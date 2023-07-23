package api.utilities;

import static io.restassured.RestAssured.given;

import java.time.Instant;
import java.util.HashMap;

import api.endpoints.Routes;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TokenManager {

	private static String access_token;
	private static Instant expiry_time;
	
	public static String getToken()
	{
		try {
			if(access_token ==null || Instant.now().isAfter(expiry_time))
			{
				System.out.println("Renewing Token ... ");
				Response response = renewToken();
				access_token= response.path("access_token");
				int expiryDurationInSeconds= response.path("expires_in");
				expiry_time= Instant.now().plusSeconds(expiryDurationInSeconds-300);
			}
			else {
				System.out.println("Token is good to use");
			}
			
		}
		catch(Exception e)
		{
			throw new RuntimeException ("Abort! Failed to get token");
		}
		return access_token;
	}
	
	public static Response renewToken()
	{
		HashMap<String, String> formParams= new HashMap<String, String>();
        formParams.put("client_id", "ConfigLoader.getInstance().getClientId()");
        formParams.put("client_secret","ConfigLoader.getInstance().getClient_secret()");
        formParams.put("grant_type","ConfigLoader.getInstance().getGrant_type()");
        formParams.put("refresh_token","ConfigLoader.getInstance().getRefreshToken()");
        
    
        		Response response= given()
				.pathParam("username", "userName")
			.when()
				.get(Routes.get_url);
        		
        		/*given().
        		.baseUri("https://accounts.spotify.com")
        		.contentType(ContentType.JSON)
        		.formParamas(formParams)
        		.when().post("/api/token").
        		then().
        		extract().
        		response();*/
        
        if(response.statusCode()!= 200){
            throw new RuntimeException("ABORT!!! Renew Token failed");
        }
        return response;
    }
	
	
}
