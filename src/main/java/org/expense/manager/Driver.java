package org.expense.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.expense.manager.enums.ExpenseSplitType;
import org.expense.manager.service.ExpenseService;
import org.expense.manager.service.TripService;
import org.expense.manager.service.dto.ExpenseDto;
import org.expense.manager.service.dto.TripDto;
import org.expense.manager.service.impl.ExpenseServiceImpl;
import org.expense.manager.service.impl.TripServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class Driver {
    private static TripService teamService = TripServiceImpl.getInstance();
    private static ExpenseService expenseService = ExpenseServiceImpl.getInstance();
    private static Scanner scanner = new Scanner(System.in);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static boolean isEmptyString(String str){
        return str==null || str.length()==0;
    }

    private static void printMenu(String[] options){
        for (String option : options){
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }

    private static void createTrip(){
        System.out.println("Enter trip name:");
        String tripName = scanner.next();
        if(isEmptyString(tripName)){
            System.out.println("Trip name is empty.");
            return;
        }boolean isTeamCreated = teamService.createTeam(tripName);

        if(isTeamCreated)
            System.out.println("Trip "+tripName+" created.");
        else {
            System.out.println("Trip "+tripName+" is already created.");
        }
    }

    private static void addTripParticipant(){
        System.out.println("Enter trip name:");
        String tripName = scanner.next();
        if(isEmptyString(tripName)){
            System.out.println("Trip name is empty");
            return;
        }
        System.out.println("Enter participant name:");
        String participant = scanner.next();
        if(isEmptyString(participant)){
            System.out.println("Participant name is empty");
            return;
        }
        boolean isParticipantAdded = teamService.addTeamParticipant(tripName,participant);
        if(isParticipantAdded){
            System.out.println("Participant "+participant + " created.");
        }else{
            System.out.println("Trip "+tripName+" is not found.");
        }
    }

    private static void addTripCategory(){
        System.out.println("Enter trip name:");
        String tripName = scanner.next();
        if(isEmptyString(tripName)){
            System.out.println("Trip name is empty");
            return;
        }
        System.out.println("Enter category name:");
        String category = scanner.next();
        if(isEmptyString(category)){
            System.out.println("Category is empty");
            return;
        }
        boolean isCategoryAdded = teamService.addTeamCategory(tripName,category);
        if(isCategoryAdded){
            System.out.println("Category "+category + " created.");
        }else{
            System.out.println("Trip "+tripName+" is not found.");
        }
    }

    private static void noteDownExpense(){
        System.out.println("Enter trip name:");
        String tripName = scanner.next();
        if(isEmptyString(tripName)){
            System.out.println("Trip name is empty");
            return;
        }
        System.out.println("Enter category name:");
        String category = scanner.next();
        if(isEmptyString(category)){
            System.out.println("Category is empty");
            return;
        }
        System.out.println("Expense made by:");
        String paidBy = scanner.next();
        if(isEmptyString(category)){
            System.out.println("Paid by name is empty:");
            return;
        }
        System.out.println("Enter the amount:");
        double amount = scanner.nextDouble();
        if(amount==0){
            System.out.println("Amount is zero:");
            return;
        }
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setExpenseSplitType(ExpenseSplitType.EQUAL);
        expenseDto.setTeam(tripName);
        expenseDto.setAmount(amount);
        expenseDto.setCategory(category);
        expenseDto.setPaidBy(paidBy);
        expenseService.addExpense(expenseDto);
        System.out.println("INR "+amount+" expense made by "+paidBy+" for "+tripName+" trip split equally.");
    }

    private static void noteDownUnequalExpense(){
        System.out.println("Enter trip name:");
        String tripName = scanner.next();
        if(isEmptyString(tripName)){
            System.out.println("Trip name is empty");
            return;
        }
        System.out.println("Enter category name:");
        String category = scanner.next();
        if(isEmptyString(category)){
            System.out.println("Category is empty");
            return;
        }
        System.out.println("Expense made by:");
        String paidBy = scanner.next();
        if(isEmptyString(category)){
            System.out.println("Paid by name is empty:");
            return;
        }
        System.out.println("Enter the amount:");
        double amount = scanner.nextDouble();
        if(amount==0){
            System.out.println("Amount is zero:");
            return;
        }
        TripDto tripDto = teamService.getTrip(tripName);
        Map<String,Double> shares = new HashMap<>();
        for(String participant : tripDto.getParticipants()){
            System.out.println(participant + " Percentage of share:");
            Double share = scanner.nextDouble();
            shares.put(participant,share);
        }
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setExpenseSplitType(ExpenseSplitType.PERCENTAGE);
        expenseDto.setTeam(tripName);
        expenseDto.setAmount(amount);
        expenseDto.setCategory(category);
        expenseDto.setPaidBy(paidBy);
        expenseDto.setShares(shares);
        expenseService.addExpense(expenseDto);

        System.out.println("INR "+amount+" expense made by "+paidBy+" for "+tripName+" trip split unequally.");
    }

    private static void showTripSummary(){
        System.out.println("Enter trip name:");
        String tripName = scanner.next();
        if(isEmptyString(tripName)){
            System.out.println("Trip name is empty");
            return;
        }
        System.out.println(gson.toJson(expenseService.getTeamExpenseSummary(tripName)));
    }

    private static void showTripPaymentSummary(){
        System.out.println("Enter trip name:");
        String tripName = scanner.next();
        if(isEmptyString(tripName)){
            System.out.println("Trip name is empty");
            return;
        }
        System.out.println(gson.toJson(expenseService.getTransitivePaymentSummary(tripName)));
    }



    public static void main(String[] args) {
        String[] options = {
                "1- Create Trip",
                "2- Create Participant",
                "3- Create Category",
                "4- Note down expense",
                "5- Note unequal expense",
                "6- Show summary",
                "7- Calculate transitive payment",
                "8- Exit",
        };

        int option = 0;
        while (option!=8){
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option){
                    case 1: createTrip(); break;
                    case 2: addTripParticipant(); break;
                    case 3: addTripCategory(); break;
                    case 4: noteDownExpense(); break;
                    case 5: noteDownUnequalExpense(); break;
                    case 6: showTripSummary(); break;
                    case 7: showTripPaymentSummary(); break;
                    case 8: exit(0);
                }
            }
            catch (Exception ex){
                System.out.println("Please enter an integer value between 1 and " + options.length);
                scanner.next();
            }
        }
    }
}
