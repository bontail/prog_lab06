package server;

import shared.data.Person;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Class for save Person collection
 * All actions go through this class
 */
// TODO: add dumping after command
public class ServerPersonManager {
    final private ArrayDeque<Person> persons = new ArrayDeque<>();
    final private Date creationDate;

    public ServerPersonManager() {
        this.creationDate = new Date();
        this.persons.addAll(Dumper.getDumpPersons());
    }

    private String getCollectionType(){
        return this.persons.getClass().getSimpleName();
    }

    private int getLength(){
        return this.persons.size();
    }

    public void save(){
        Dumper.saveDump(this.getPersons());
    }

    public Boolean addPerson(Boolean ifMax, Person person) {
        ArrayList<Person> personsArray = new ArrayList<>(this.persons);
        personsArray.add(person);
        personsArray.sort(Person::compareTo);
        if (ifMax && personsArray.get(personsArray.size() - 1) != person) {
            return false;
        }
        this.persons.clear();
        this.persons.addAll(personsArray);
        return true;
    }

    public void removePersons(List<Person> persons) {
        persons.forEach(this.persons::removeFirstOccurrence);
    }

    public ArrayList<Person> removeLowerPersons(Person person) {
        ArrayList<Person> removedPersons = new ArrayList<>();
        ArrayList<Person> personsArray = new ArrayList<>(this.persons);
        personsArray.add(person);
        personsArray.sort(Person::compareTo);
        for (var it = personsArray.iterator(); it.hasNext();){
            Person p = it.next();
            if (p.equals(person)) {
                it.remove();
                break;
            }
            removedPersons.add(p);
        }
        return removedPersons;
    }

    public void clear(){
        this.persons.clear();
    }

    public List<Person> getPersons() {
        return new ArrayList<>(this.persons);
    }

    public String getInfo(){
        return "Type: " + this.getCollectionType() + '\n' + "Creation Date: " + this.creationDate + '\n' + "Length: " + this.getLength();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Person person : this.persons) {
            sb.append(person).append("\n");
        }
        sb.append(']');
        return sb.toString();
    }
}
