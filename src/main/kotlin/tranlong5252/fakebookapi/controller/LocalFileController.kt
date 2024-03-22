package tranlong5252.fakebookapi.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tranlong5252.fakebookapi.dto.localfiles.LocalFileDto
import tranlong5252.fakebookapi.dto.localfiles.LocalFileUrlDto
import tranlong5252.fakebookapi.service.LocalFileService

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

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): ByteArray {
        val response  = service.getFileInfo(id)
        return response.readBytes()
    }
    
}