package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.carDTOs.ImportCarRootDTO;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CarServiceImpl implements CarService {

    public static final String  CARS_FILE_PATH = "src/main/resources/files/xml/cars.xml";

    private final CarRepository carRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public CarServiceImpl(CarRepository carRepository,
                          XmlParser xmlParser, ModelMapper modelMapper,
                          ValidationUtil validationUtil) {
        this.carRepository = carRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {

        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files
                .readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser
                .fromFile(CARS_FILE_PATH, ImportCarRootDTO.class)
                .getCars()
                .stream()
                .filter(importCarDTO -> {
            boolean isValid = validationUtil.isValid(importCarDTO);

 //////////// if the plate_number already exists in the DB return Invalid
            boolean doesntExist = carRepository
                    .findCarByPlateNumber(importCarDTO.getPlateNumber())
                    .isEmpty();

            if (!doesntExist) {
                isValid = false;
            }

  ////////////////////////////////////////////////////////////////////////////
            sb
                    .append(isValid
                            ? String.format("Successfully imported car %s - %s",
                            importCarDTO.getCarMake(),
                            importCarDTO.getCarModel())
                            : "Invalid car")
                    .append(System.lineSeparator());

            return isValid;
        })
                .map(importCarDTO -> modelMapper.map(importCarDTO, Car.class))
        .forEach(carRepository::save);

        return sb.toString();

    }

    @Override
    public Car findCarByPlateNumber(String plateNumber) {
        return carRepository
                .findCarByPlateNumber(plateNumber)
                .orElse(null);
    }

    @Override
    public Car findCarById(Long id) {
        return carRepository
                .getById(id);
    }
}
