package hexlet.code.service.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.exception.CustomNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;

    @Override
    public Label createNewLabel(LabelDto labelDto) {
        return labelRepository.save(new Label(labelDto.getName()));
    }

    @Override
    public Label updateExistingLabel(long id, LabelDto labelDto) {
        Label labelToUpdate = labelRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Label"));
        labelToUpdate.setName(labelDto.getName());
        return labelRepository.save(labelToUpdate);
    }
}
