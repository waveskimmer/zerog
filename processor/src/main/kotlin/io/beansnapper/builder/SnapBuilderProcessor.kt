package io.beansnapper.builder


import com.squareup.kotlinpoet.*
import io.beansnapper.annotations.SnapBuilder
import kotlinx.metadata.Flag
import kotlinx.metadata.KmClass
import kotlinx.metadata.KmClassifier
import kotlinx.metadata.KmType
import kotlinx.metadata.jvm.KotlinClassMetadata
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic.Kind.ERROR
import javax.tools.Diagnostic.Kind.WARNING

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("io.beansnapper.annotations.SnapBuilder")
@SupportedOptions("kapt.kotlin.generated")
class SnapBuilderProcessor : AbstractProcessor() {
    private val myAnnotation = SnapBuilder::class.qualifiedName!!

    private fun printError(message: String) {
        processingEnv.messager.printMessage(ERROR, message)
    }

    private fun printError(message: String, element: Element) {
        processingEnv.messager.printMessage(ERROR, message, element)
    }

    private fun printWarning(message: String, element: Element) {
        processingEnv.messager.printMessage(WARNING, message, element)
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        val annotatedElements = roundEnv.getElementsAnnotatedWith(SnapBuilder::class.java)
        if (annotatedElements.isEmpty()) return false

        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"] ?: run {
            processingEnv.messager.printMessage(ERROR, "Can't find the target directory for generated Kotlin files.")
            return false
        }
        val genDir = File(kaptKotlinGeneratedDir)
        genDir.mkdirs()

        for (element in annotatedElements) {
            val metadata: KotlinClassMetadata? = element.kotlinMetadata
            if (element.kind == ElementKind.CLASS
                && element is TypeElement
                && metadata != null
                && metadata is KotlinClassMetadata.Class
            ) {
                val fileSpec = generateBuilder(metadata.toKmClass(), element)
                fileSpec.writeTo(genDir)
            } else {
                printError("$myAnnotation Annotation not applied to Kotlin Class", element)
            }
        }
        return true
    }

    private fun generateBuilder(kmClass: KmClass, typeElement: TypeElement): FileSpec {
        val packageName = findPackageName(typeElement)
        val name = typeElement.simpleName.toString()
        val target = ClassName(packageName, name)
        val genName = name + "Builder"

        val primaryConst = kmClass.constructors.first { Flag.Constructor.IS_SECONDARY.invoke(it.flags).not() }
        val params = primaryConst.valueParameters.map {
            ParameterSpec.builder(it.name, createTypeName(it.type)).build()
        }
        val paramNames = primaryConst.valueParameters.map { it.name }.toSet()

        val properties = params.map {
            val builder = PropertySpec.builder(it.name, it.type).mutable()
            if (paramNames.contains(it.name)) {
                builder.initializer(it.name)
            }
            builder.build()
        }

        return FileSpec.builder(packageName, genName)
            .addFunction(
                FunSpec.builder("toBuilder")
                    .receiver(target)
                    .addStatement(
                        "return $genName(" + primaryConst.valueParameters.joinToString { it.name } + ")"
                    )
                    .build()
            )
            .addType(
                TypeSpec.classBuilder(genName)
                    .primaryConstructor(
                        FunSpec.constructorBuilder().addParameters(params).build()
                    )
                    .addProperties(properties)
                    .addFunction(genBuildMethod(target, params))
                    .build()
            )
            .build()
    }

    private fun createTypeName(returnType: KmType?): TypeName =
        when (val classifier = returnType?.classifier) {
            null -> UNIT
            is KmClassifier.Class -> {
                val names = classifier.name.split("/")
                val packageName = names.subList(0, names.size - 1).joinToString(".")
                val simpleName = names.last()
                ClassName(packageName, simpleName)
            }
            else -> throw Exception("unhandled classifier")
        }

    private fun findPackageName(typeElement: TypeElement): String {
        val enclosing = typeElement.enclosingElement
        if (enclosing is PackageElement) {
            return enclosing.qualifiedName.toString()
        } else throw Exception("currently only handle top level packages")
    }

    private fun genBuildMethod(target: ClassName, params: List<ParameterSpec>): FunSpec {
        val buildIt = StringBuilder("return ")
            .append(target.simpleName)
            .append("(")

        params.forEach {
            buildIt.append("${it.name},")
        }
        buildIt.append(")")

        return FunSpec.builder("build")
            .returns(target)
            .addStatement(buildIt.toString())
            .build()
    }

}
