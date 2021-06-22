package Data;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Pet {
    private Integer id;
    private HashMap<String, Object> category;
    private String name;
    private ArrayList<String> photoUrls;
    private ArrayList<HashMap<String, Object>> tags;
    private String status;
}







