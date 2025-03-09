package shared.responses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shared.data.Person;

import java.util.ArrayList;
import java.util.Comparator;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonsResponse extends Response {
    public Boolean isSuccess;
    public ArrayList<Person> persons;

    public String jsonData() throws JsonProcessingException {
        this.persons.sort(Comparator.comparing(Person::toString));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
