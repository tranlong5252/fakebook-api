package tranlong5252.fakebookapi.dto.localfiles

import jakarta.validation.constraints.NotEmpty
import java.io.File

class LocalFileDto {
    @NotEmpty
    lateinit var file: File private set
}