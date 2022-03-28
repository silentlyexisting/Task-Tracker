package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

public interface LabelService {
    Label createNewLabel(LabelDto labelDto);
    Label updateExistingLabel(long id, LabelDto labelDto);
}
