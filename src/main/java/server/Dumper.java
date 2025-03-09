package server;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import shared.data.Person;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static java.lang.System.exit;


/**
 * Class for working with backup and dump
 */
public class Dumper {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Dumper.class);

    public static String getCollectionFilename(){
        String fileName = System.getenv("COLLECTION_FILENAME");
        if (fileName == null) {
            logger.error("No environment variable COLLECTION_FILENAME found");
            return null;
        }
        return fileName;
    }

    public static void saveDump(List<Person> persons) {
        String fileName = getCollectionFilename();
        if (fileName == null) {
            exit(1);
        }
        Dumper.write(fileName, persons);
    }

    public static List<Person> getDumpPersons(){
        String fileName = getCollectionFilename();
        if (fileName == null) {
            exit(1);
        }
        return Dumper.read(fileName);
    }

    private static List<Person> read(String fileName) {
        try{
            Path path = Path.of(fileName);
            if(!Files.exists(path)) {
                Files.createFile(path);
            }

            Reader myReader = new FileReader(fileName, StandardCharsets.UTF_8);
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(Person.class)
                    .withColumnSeparator(';').withSkipFirstDataRow(true);
            MappingIterator<Person> iterator = mapper
                    .readerFor(Person.class)
                    .with(schema)
                    .readValues(myReader);
            ArrayList<Person> personsArray = (ArrayList<Person>) iterator.readAll();
            Collections.sort(personsArray);
            HashSet<Integer> ids = new HashSet<>();
            for (Person person : personsArray) {
                boolean isAdded = ids.add(person.getId());
                Person.updateLastId(person.getId());
                if (!isAdded) {
                    logger.error("Data contains non unique id {}", person.getId());
                    return null;
                }
            }
            return personsArray;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void write(String fileName, List<Person> persons) {
        try {
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(Person.class)
                    .withColumnSeparator(';')
                    .withHeader();
            ObjectWriter writer = mapper.writer(schema);
            writer.writeValue(new FileWriter(fileName, StandardCharsets.UTF_8), persons);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
