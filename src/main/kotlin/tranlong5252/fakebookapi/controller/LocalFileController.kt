package tranlong5252.fakebookapi.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tranlong5252.fakebookapi.dto.localfiles.LocalFileDto
import tranlong5252.fakebookapi.dto.localfiles.LocalFileUrlDto
import tranlong5252.fakebookapi.exception.FakebookException
import tranlong5252.fakebookapi.exception.errors.EntityNotFoundErrorReport
import tranlong5252.fakebookapi.service.LocalFileService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.server.ServerHttpResponse

@RestController
@RequestMapping("/local-files")
class LocalFileController {
    @Autowired
    private lateinit var service: LocalFileService

    @PostMapping("/", consumes = ["multipart/form-data"])
    fun create(file: MultipartFile, @ModelAttribute resDto: LocalFileDto): ResponseEntity<LocalFileUrlDto> {
        val response  = service.create(file, resDto)
        return ResponseEntity.ok().body(response)
    }

    @ResponseBody
    @GetMapping("/{id}", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun get(@PathVariable id: String): ByteArray {
        val file = service.getFileInfo(id) ?:
        throw FakebookException(EntityNotFoundErrorReport("id", id))
        return file.readBytes()
    }
}