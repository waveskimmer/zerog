package io.beansnapper.builder

import com.squareup.kotlinpoet.*
import io.kotest.core.spec.style.StringSpec
import javax.lang.model.element.TypeElement

//class SnapBuilderTest : StringSpec({
//
//    "simple generate code" {
//        println("Success")
//    }

//    "simple generate code" {
//        val greeterClass = ClassName("", "Greeter")
//        val file = FileSpec.builder("", "HelloWorld")
//            .addType(
//                TypeSpec.classBuilder("Greeter")
//                .primaryConstructor(FunSpec.constructorBuilder()
//                    .addParameter("name", String::class)
//                    .build())
//                .addProperty(
//                    PropertySpec.builder("name", String::class)
//                    .initializer("name")
//                    .build())
//                .addFunction(FunSpec.builder("greet")
//                    .addStatement("println(\"Hello, \$name\")")
//                    .build())
//                .build())
//            .addFunction(
//                FunSpec.builder("main")
//                .addParameter("args", String::class, KModifier.VARARG)
//                .addStatement("%T(args[0]).greet()", greeterClass)
//                .build())
//            .build()
//
//        file.writeTo(System.out)
//    }


//})
