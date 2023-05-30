
import khttp.post
import org.json.JSONArray
import org.json.JSONObject

fun executePostRequest(url: String, headers: Map<String, String>, body: JSONObject): JSONObject {
    val response = post(url, headers = headers, json = body)
    return response.jsonObject
}
fun main() {
    val url = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/address"
    val headers = mapOf(
        "Content-Type" to "application/json",
        "Accept" to "application/json",
        "Authorization" to "Token afb6efc7316df819b86ac2bda89d344dbafaad60"
    )
    val body = JSONObject()
    body.put("query", "Новосибирск Некрасова 82")

    val response = executePostRequest(url, headers, body)
    val suggestions = response.getJSONArray("suggestions")

    val insertValues = StringBuilder()
    for (i in 0 until suggestions.length()) {
        val suggestion = suggestions.getJSONObject(i)
        //val value = suggestion.optString("value")
        val data = suggestion.optJSONObject("data")
        val cityDistrict = data?.optString("city_district")
        val streetWithType = data?.optString("street_with_type")
        val houseNumber = data?.optString("house")

        // Add values to the INSERT statement
        insertValues.append("('$cityDistrict', '$streetWithType', '$houseNumber'),\n")
    }
    // Remove the trailing comma
    val values = insertValues.toString().removeSuffix(",")

    // Generate the SQL query
    val sqlQuery = "INSERT INTO your_table_name (area, street, house_number) VALUES $values"

    println("SQL Query:")
    println(sqlQuery)
}
