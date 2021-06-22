
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import Data.Pet;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PetApiTest {

    String baseURI = "https://petstore.swagger.io/v2";
    Random random = new Random();
    Faker faker = new Faker();

    @Test
    @Description("Verify that new Pet is successfully added to the Store")
    public void verifyAddingANewPetToTheStore() {

        Pet pet = getPet();

        //GIVEN
        Pet result = RestAssured.given().baseUri(baseURI).contentType(ContentType.JSON).body(pet)
                // WHEN
                .when().post("/pet")
                // THEN
                .then().assertThat().statusCode(200).extract().as(Pet.class);

        assertThat(result, samePropertyValuesAs(pet));
    }

    @Test
    @Description("Verify that updating of existing pet information")
    public void verifyUpdatingPetToTheStore() {

        Pet pet = getPet();
        var photoUrls = new ArrayList<String>();
        photoUrls.add("c.png");
        pet.setPhotoUrls(photoUrls);

        //GIVEN
        Pet result = RestAssured.given().baseUri(baseURI).contentType(ContentType.JSON).body(pet)
                // WHEN
                .when().put("/pet")
                // THEN
                .then().assertThat().statusCode(200).extract().as(Pet.class);

        assertThat(result, samePropertyValuesAs(pet));

    }

    @Test
    @Description("Verify that updating of existing pet information with Invalid ID")
    public void verifyUpdatingPetToTheStoreWithInvalidId() {

        Pet pet = getPet();
        pet.setId(-9);

        //GIVEN
        RestAssured.given().baseUri(baseURI).contentType(ContentType.JSON).body(pet)
                // WHEN
                .when().put("/pet")
                // THEN
                .then().assertThat().statusCode(400);

    }

    @Test
    @Description("Verify that updating of existing pet information with Non Existing Pet")
    public void verifyUpdatingPetToTheStoreWithNonExistingPet() {

        Pet pet = getPet();
        System.out.println(pet.getId());
        System.out.println(pet.getName());

        //GIVEN
        RestAssured.given().baseUri(baseURI).contentType(ContentType.JSON).body(pet)
                // WHEN
                .when().put("/pet")
                // THEN
                .then().assertThat().statusCode(404);

    }

    @Test
    @Description("Verify updating the pet with form data")
    public void verifyUpdatingThePetWithFormData() {

        Pet pet = getPet();

        //GIVEN
        RestAssured.given().baseUri(baseURI).accept(ContentType.JSON).contentType("application/x-www-form-urlencoded")
                .formParam("name", faker.name().firstName())
                .formParam("status", "sold").log().all()
                // WHEN
                .when().post("/pet/" + pet.getId())
                // THEN
                .then().assertThat().statusCode(200).log().all();
    }

    @Test
    @Description("Verify updating the pet with form data containing invalid input")
    public void verifyUpdatingThePetWithFormDataAndInvalidInput() {

        Pet pet = getPet();

        //GIVEN
        RestAssured.given().baseUri(baseURI).accept(ContentType.JSON).contentType("application/x-www-form-urlencoded")
                .formParam("name", faker.name().firstName())
                .formParam("status", "notsold")
                // WHEN
                .when().post("/pet/" + pet.getId())
                // THEN
                .then().assertThat().statusCode(405);
    }


    @Test(dataProvider = "fetchPetStatus")
    @Description("Verify retrieving of the pet as per valid status")
    public void verifyRetrievePetsPerStatus(String petStatus) {

        //GIVEN
        RestAssured.given().baseUri(baseURI)
                // WHEN
                .when().get("/pet/findByStatus?status=" + petStatus)
                // THEN
                .then().assertThat().statusCode(200);

    }

    @Test(dataProvider = "fetchMultiplePetStatus")
    @Description("Verify retrieving of the pet as per valid status")
    public void verifyRetrievePetsPerStatus(String petStatus1, String petStatus2) {

        //GIVEN
        RestAssured.given().baseUri(baseURI)
                // WHEN
                .when().get("/pet/findByStatus?status=" + petStatus1 + "&status=" + petStatus2)
                // THEN
                .then().assertThat().statusCode(200);

    }

    @Test
    @Description("Verify retrieving of the pet with invalid status")
    public void verifyRetrievePetsWithInvalidStatus() {

        //GIVEN
        RestAssured.given().baseUri(baseURI)
                // WHEN
                .when().get("/pet/findByStatus?status=inprogress")
                // THEN
                .then().assertThat().statusCode(400);

    }

    @Test
    @Description("Verify retrieving of the pet with valid and invalid id")
    public void verifyRetrievalOfsPetsByInvalidId() {

        //GIVEN
        RestAssured.given().baseUri(baseURI)
                // WHEN
                .when().get("/pet/" + 9877)
                // THEN
                .then().assertThat().statusCode(404);

    }

    @Test
    @Description("Verify retrieving of the pet with valid and invalid id")
    public void verifyRetrievalOfsPetsByValidId() {

        System.out.println("verifyRetrievalOfsPetsByValidId");

        Pet pet = getPet();

        //GIVEN
        RestAssured.given().baseUri(baseURI)
                // WHEN
                .when().get("/pet/" + pet.getId())
                // THEN
                .then().assertThat().statusCode(200).log().ifValidationFails();

    }


    @Test
    public void verifyUploadOfImage() {

        System.out.println("verifyImageUpload");

        File testUploadFile = new File("src/main/resources/Sample-png-Image-for-Testing.png");

        Pet pet = getPet();

        //GIVEN
        RestAssured.given().baseUri(baseURI).accept(ContentType.JSON)
                .formParam("additionalMetadata", "uploadImage")
                .multiPart("file", testUploadFile)
                // WHEN
                .when().post("/pet/" + pet.getId() + "/uploadImage")
                // THEN
                .then().assertThat().statusCode(200);


    }

    @Test
    public void verifyDeletionOfPetWithInvalidId() {

        //GIVEN
        RestAssured.given().baseUri(baseURI)
                // WHEN
                .when().delete("/pet/" + -9)
                // THEN
                .then().assertThat().statusCode(404);

    }

    @Test
    public void verifyDeletionOfPetWithValidId() {

        Pet pet = getPet();

        //GIVEN
        RestAssured.given().baseUri(baseURI)
                // WHEN
                .when().delete("/pet/" + pet.getId())
                // THEN
                .then().assertThat().statusCode(200);

    }

    @DataProvider(name = "fetchPetStatus")
    public Object[][] getPetStatus() {
        return new Object[][]{{"pending"},
                {"available"},
                {"sold"}
        };
    }

    @DataProvider(name = "fetchMultiplePetStatus")
    public Object[][] getMultiplePetStatus() {
        return new Object[][]{{"pending", "available"},
                {"available", "sold"},
                {"sold", "pending"}
        };
    }

    private Pet getPet() {
        Integer testPetId = random.nextInt(1000);
        String testName = faker.name().firstName();

        var categoryMap = new HashMap<String, Object>();
        categoryMap.put("id", random.nextInt(1000));
        categoryMap.put("name", "testCategory");

        var tag1Map = new HashMap<String, Object>();
        tag1Map.put("id", random.nextInt(1000));
        tag1Map.put("name", "testTag1");
        var tag2Map = new HashMap<String, Object>();
        tag2Map.put("id", random.nextInt(1000));
        tag2Map.put("name", "testTag2");
        var tagMap = new ArrayList<HashMap<String, Object>>();
        tagMap.add(tag1Map);
        tagMap.add(tag2Map);

        var photoUrls = new ArrayList<String>();
        photoUrls.add("a.png");
        photoUrls.add("b.png");

        Pet pet = Pet.builder().id(testPetId).category(categoryMap).name(testName)
                .photoUrls(photoUrls).tags(tagMap).status("available").build();
        return pet;
    }


}