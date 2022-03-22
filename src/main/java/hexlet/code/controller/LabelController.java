package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {
    private static final String ID = "/{id}";
    public static final String LABEL_CONTROLLER_PATH = "/labels";

    private final LabelRepository labelRepository;
    private final LabelService labelService;

    @PostMapping
    public Label createLabel(@RequestBody @Valid LabelDto labelDto) {
        return labelService.createLabel(labelDto);
    }

    @GetMapping(ID)
    public Label getLabelById(@PathVariable(name = "id") long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Label"));
    }

    @GetMapping
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @PutMapping(ID)
    public Label updateLabelById(@PathVariable(name = "id") long id,
                                 @RequestBody @Valid LabelDto labelDto) {
        return labelService.updateLabel(id, labelDto);
    }

    @DeleteMapping(ID)
    public void deleteLabelById(@PathVariable(name = "id") long id) {
        labelRepository.deleteById(id);
    }
}
