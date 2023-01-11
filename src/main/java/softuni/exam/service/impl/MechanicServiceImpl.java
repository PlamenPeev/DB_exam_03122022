package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportMechanicDTO;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class MechanicServiceImpl implements MechanicService {

    public static final String MECHANICS_FILE_PATH = "src/main/resources/files/json/mechanics.json";

    private final MechanicRepository mechanicRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public MechanicServiceImpl(MechanicRepository mechanicRepository, Gson gson,
                               ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.mechanicRepository = mechanicRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {

        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        return Files
                .readString(Path.of(MECHANICS_FILE_PATH));
    }

    @Override
    public String importMechanics() throws IOException {

        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readMechanicsFromFile(), ImportMechanicDTO[].class))
                .filter(importMechanicDTO -> {
                    boolean isValid = validationUtil.isValid(importMechanicDTO);

//////////// if already exists e-mail in the DB return Invalid ///////////////////////////////

                    boolean doesntExist = mechanicRepository
                            .findMechanicByEmail(importMechanicDTO.getEmail())
                            .isEmpty();

                    if (!doesntExist) {
                        isValid = false;
                    }
/////////////////////////////////////////////////////////////////////////////////////////////


                    sb
                            .append(isValid
                                    ? String.format("Successfully imported mechanic %s %s",
                                    importMechanicDTO.getFirstName(),
                                    importMechanicDTO.getLastName())
                                    : "Invalid mechanic")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(importMechanicDTO -> modelMapper.map(importMechanicDTO, Mechanic.class))
                .forEach(mechanicRepository::save);

        return sb.toString();

    }

    @Override
    public Mechanic findMechanicByEmail(String email) {
        return mechanicRepository
                .findMechanicByEmail(email)
                .orElse(null);
    }

    @Override
    public Mechanic findMechanicalByFirstName(String firstName) {
        return mechanicRepository
                .findMechanicalByFirstName(firstName)
                .orElse(null);
    }

}
