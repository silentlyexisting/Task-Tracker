package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {

    private final LabelRepository labelRepository;
    private final LabelService labelService;

    public static final String LABEL_CONTROLLER_PATH = "/labels";
    private static final String ID = "/{id}";

    @Operation(summary = "Create new label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Invalid data")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Label createLabel(@Parameter(description = "Label data") @RequestBody @Valid LabelDto labelDto) {
        return labelService.createNewLabel(labelDto);
    }

    @Operation(summary = "Get label by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @GetMapping(ID)
    public Label getLabelById(@Parameter(description = "Label id") @PathVariable(name = "id") long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Label"));
    }

    @Operation(summary = "Get list of all task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Received successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Operation(summary = "Change label data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Changed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Label not found"),
            @ApiResponse(responseCode = "500", description = "Invalid data")
    })
    @PutMapping(ID)
    public Label updateLabelById(@Parameter(description = "Label id") @PathVariable(name = "id") long id,
                                 @Parameter(description = "Label data") @RequestBody @Valid LabelDto labelDto) {
        return labelService.updateExistingLabel(id, labelDto);
    }

    @Operation(summary = "Delete label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "422", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Label not found")
    })
    @DeleteMapping(ID)
    public void deleteLabelById(@Parameter(description = "Label id") @PathVariable(name = "id") long id) {
        labelRepository.deleteById(id);
    }
}
