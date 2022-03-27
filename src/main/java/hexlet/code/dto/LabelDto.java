package hexlet.code.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class LabelDto {

    @Size(min = 1, message = "Label name must contain at least 1 character")
    private String name;

}
