package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.taskDTOs.ImportTaskRootDTO;
import softuni.exam.models.entity.Task;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PartRepository;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.MechanicService;
import softuni.exam.service.TaskService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    public static final String TASKS_FILE_PATH = "src/main/resources/files/xml/tasks.xml";

    private final TaskRepository taskRepository;
    private final CarRepository carRepository;
    private final MechanicService mechanicService;
    private final PartRepository partRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TaskServiceImpl(TaskRepository taskRepository,
                           CarRepository carRepository, MechanicService mechanicService,
                           PartRepository partRepository, XmlParser xmlParser, ModelMapper modelMapper,
                           ValidationUtil validationUtil) {
        this.taskRepository = taskRepository;
        this.carRepository = carRepository;
        this.mechanicService = mechanicService;
        this.partRepository = partRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {

        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files
                .readString(Path.of(TASKS_FILE_PATH));
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        xmlParser
                .fromFile(TASKS_FILE_PATH, ImportTaskRootDTO.class)
                .getTasks()
                .stream()
                .filter(importTaskDTO -> {
                    boolean isValid = validationUtil.isValid(importTaskDTO);

////////////////// if the name doesn’t already exist in the DB return Invalid //////////////
                    if (mechanicService
                            .findMechanicalByFirstName(importTaskDTO.getMechanic().getFirstName()) ==null){
                        isValid = false;
                    }
//////////////////////////////////////////////////////////////////////////////////

                    sb
                            .append(isValid
                                    ? String.format("Successfully imported task %.2f",
                                    importTaskDTO.getPrice())
                                    : "Invalid task")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(importTaskDTO -> {
                    Task task= modelMapper.map(importTaskDTO, Task.class);
                    task.setCar(carRepository.getById(importTaskDTO.getCar().getId()));
                    task.setMechanic(mechanicService.findMechanicalByFirstName(importTaskDTO.getMechanic().getFirstName()));
                    task.setPart(partRepository.getById(importTaskDTO.getPart().getId()));
                    return task;
                })
                .forEach(taskRepository::save);

        return sb.toString();

    }


    @Override
    public String getCoupeCarTasksOrderByPrice() throws JAXBException, IOException {

        StringBuilder sb = new StringBuilder();

        List<Task> taskList = taskRepository
                .findCarTypeCoupe();

        taskList
                .forEach(task ->{
                    sb.append(String.format("Car %s %s with %dkm%n" +
                                            "-Mechanic: %s %s - task №%d:%n" +
                                            "--Engine: %.2f%n" +
                                            "---Price: %.2f$%n",
                            task.getCar().getCarMake(), task.getCar().getCarModel(),task.getCar().getKilometers(),
                            task.getMechanic().getFirstName(), task.getMechanic().getLastName(),
                            task.getId(),
                            task.getCar().getEngine(),
                            task.getPrice()


                                   ))
                            .append(System.lineSeparator());
                });
        
        return sb.toString();
    }
}
