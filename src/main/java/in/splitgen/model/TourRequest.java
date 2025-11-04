package in.splitgen.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourRequest {
    @NotBlank(message = "Provide a valid tour name")
    @Size(min = 3, max = 15, message = "Tour name must be between 3 and 15 characters.")
    private String name;
    private String description;
    private String startDate;
    private String endDate;

}
