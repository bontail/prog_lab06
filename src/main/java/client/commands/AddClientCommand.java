package client.commands;

import lombok.AllArgsConstructor;
import client.ClientInvoker;
import client.ClientPersonManager;
import shared.responses.ValidatePersonArgsResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@AllArgsConstructor
public class AddClientCommand implements ClientCommand {
    protected ClientPersonManager personManager;
    protected ClientInvoker invoker;

    @Override
    public Boolean execute(String[] personArgs) {
        ArrayList<String> personInitArgs = getPersonInitArgs(personArgs);

        System.out.println(this.personManager.addPerson(personInitArgs).message);
        return true;
    }

    protected ArrayList<String> getPersonInitArgs(String[] personArgs) {
        Long stage = 1L;
        ArrayList<String> personsArgsArray = new ArrayList<>(Arrays.asList(personArgs));
        ValidatePersonArgsResponse response = personManager.validatePersonArgs(stage, personsArgsArray);
        while (response.invalidField != null) {
            System.out.println(response.invalidField.message());
            System.out.println("Please write valid value for: " + response.invalidField.name());
            String[] new_args = this.invoker.getLineTokens();
            if (new_args.length != 1) {
                System.out.println("Please write one value");
                continue;
            }

            personsArgsArray.set(response.invalidField.index(), new_args[0]);
            response = personManager.validatePersonArgs(stage, personsArgsArray);
        }
        stage++;
        ArrayList<String> personInitArgs = new ArrayList<>(personsArgsArray);
        while (response.nextFieldName != null || response.invalidField != null) {
            System.out.println("Please write: " + response.nextFieldName);
            String[] new_args = this.invoker.getLineTokens();
            if (new_args.length != 1) {
                System.out.println("Please write one value");
                continue;
            }
            response = personManager.validatePersonArgs(stage, new ArrayList<>(List.of(new_args)));

            while (response.invalidField != null) {
                System.out.println(response.invalidField.message());
                System.out.println("Please write valid value for: " + response.invalidField.name());
                new_args = this.invoker.getLineTokens();
                if (new_args.length != 1) {
                    System.out.println("Please write one value");
                    continue;
                }
                response = personManager.validatePersonArgs(stage, new ArrayList<>(List.of(new_args)));
            }
            stage++;
            personInitArgs.add(new_args[0]);
        }
        return personInitArgs;
    }


    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getHelpInfo() {
        return "Add a person to collection";
    }

    @Override
    public ArrayList<String> getArgs(){
        return new ArrayList<>(Arrays.asList("name", "height", "weight", "hairColor", "nationality"));
    }
}