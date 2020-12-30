package inertia.controller

import inertia.domain.Task
import inertia.service.TaskService
import io.micronaut.http.annotation.*
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

@Controller("/task")
class TaskController(taskService: TaskService) {

    @Get("/{id}")
    fun find(@PathVariable id: String): Task {
        log.debug("find player with id = $id")
        return taskService.find(id)
    }

    @Post
    fun save(player: Player): Player {
        log.debug("save $player")
        return gameService.save(player)
    }

    @Delete("/{id}")
    fun delete(@PathVariable id: String) {
        log.debug("delete player id=$id")
        return gameService.delete(id)
    }



}