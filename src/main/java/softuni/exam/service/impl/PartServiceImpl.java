package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportPartDTO;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;


@Service
public class PartServiceImpl implements PartService {

    public static final String PARTS_FILE_PATH = "src/main/resources/files/json/parts.json";

    private final PartRepository partRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public PartServiceImpl(PartRepository partRepository,
                           Gson gson, ModelMapper modelMapper,
                           ValidationUtil validationUtil) {
        this.partRepository = partRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {

        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        return Files
                .readString(Path.of(PARTS_FILE_PATH));
    }

    @Override
    public String importParts() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readPartsFileContent(), ImportPartDTO[].class))
            .filter(importPartDTO -> {
            boolean isValid = validationUtil.isValid(importPartDTO);

                boolean doesntExist = partRepository
                        .findPartByPartName(importPartDTO.getPartName())
        .isEmpty();

                if (!doesntExist) {
                    isValid = false;
                }


            sb
                    .append(isValid
                            ? String.format("Successfully imported part %s - %.2f",
                                importPartDTO.getPartName(),
                            importPartDTO.getPrice())
                                : "Invalid part")
                        .append(System.lineSeparator());
            return isValid;
        })
            .map(importPartDTO -> modelMapper.map(importPartDTO, Part.class))
            .forEach(partRepository::save);

        return sb.toString();

    }

    @Override
    public Part findPartByPartName(String partName) {
        return partRepository
                .findPartByPartName(partName)
                .orElse(null);
    }

    @Override
    public Part findPartById(Long id) {
        return partRepository
                .getById(id);
    }
}
