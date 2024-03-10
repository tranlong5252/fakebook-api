package tranlong5252.fakebookapi.dto

class GetManyResponse<T>(
    val page: Int,
    val take : Int,
    val data: List<T>,
    val totalRecords: Int,
    val totalPage: Int,
    val nextPage: Int
)

