package softuni.exam.service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;


public interface TaskService {

    boolean areImported();

    String readTasksFileContent() throws IOException;

    String importTasks() throws IOException, JAXBException;

    String getCoupeCarTasksOrderByPrice() throws JAXBException, IOException;
}
