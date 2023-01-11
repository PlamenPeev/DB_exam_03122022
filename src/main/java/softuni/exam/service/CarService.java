package softuni.exam.service;


import softuni.exam.models.entity.Car;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Optional;


public interface CarService {

    boolean areImported();

    String readCarsFromFile() throws IOException;

    String importCars() throws IOException, JAXBException;

    Car findCarByPlateNumber(String plateNumber);

    Car findCarById(Long id);
}
