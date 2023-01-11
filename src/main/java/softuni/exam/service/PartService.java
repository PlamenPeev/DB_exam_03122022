package softuni.exam.service;

import softuni.exam.models.entity.Part;

import java.io.IOException;
import java.util.Optional;


public interface PartService {

    boolean areImported();

    String readPartsFileContent() throws IOException;

    String importParts() throws IOException;

    Part findPartByPartName(String partName);

    Part findPartById(Long id);
}
