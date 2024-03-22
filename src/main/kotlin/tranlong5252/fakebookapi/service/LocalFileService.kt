package tranlong5252.fakebookapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import tranlong5252.fakebookapi.db.LocalFileRepository
import tranlong5252.fakebookapi.dto.localfiles.LocalFileDto
import tranlong5252.fakebookapi.dto.localfiles.LocalFileUrlDto
import tranlong5252.fakebookapi.model.LocalFile
import tranlong5252.fakebookapi.utils.dateToString
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Date


@Service
class LocalFileService {

    @Autowired
    private lateinit var repository: LocalFileRepository

    @Value("\${app.upload-folder}")
    private lateinit var uploadFolder: String

    fun create(file: MultipartFile, @RequestBody resDto: LocalFileDto): LocalFileUrlDto {
        val dir = File(uploadFolder)
        if (!(dir.exists() && dir.isDirectory())) {
            dir.mkdir()
        }
        val date = dateToString(Date())
        val fileName = file.originalFilename ?: "file_$date"
        val localFilePath = "$uploadFolder/${fileName}_$date"
        val f = File(localFilePath)
        if (!f.exists()) {
            f.createNewFile()
            val bufferedInputStream = BufferedInputStream(file.inputStream)
            val fos = FileOutputStream(f)
            bufferedInputStream.transferTo(fos)
            fos.close()
            bufferedInputStream.close()
        }
        val entity = createLocalFile(fileName, localFilePath)

        return LocalFileUrlDto().apply {
            this.url = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() + "/local-files/${entity.id}"
        }
    }

    fun getFileInfo(id: String) = File(repository.findById(id).orElseThrow { Exception("File not found!") }.diskPath)

    private fun createLocalFile(name: String, path: String): LocalFile {
        val entity = LocalFile().apply {
            this.fileName = name
            this.diskPath = path
        }
        return repository.save(entity)
    }
}