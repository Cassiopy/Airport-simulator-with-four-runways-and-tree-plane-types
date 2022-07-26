package ar.edu.unsl.mys.engine;

import java.util.LinkedList;
import java.util.List;
//import ar.edu.unsl.mys.resources.CustomQueue;
//import java.util.Scanner;
//import java.util.Iterator;
import java.io.FileWriter;
//import java.util.ArrayList;
import java.io.BufferedWriter;

import ar.edu.unsl.mys.App;
//import ar.edu.unsl.mys.App;
//import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.entities.aircrafts.*;
import ar.edu.unsl.mys.entities.maintenance.*;
import ar.edu.unsl.mys.events.ArrivalEvent;
import ar.edu.unsl.mys.events.StopExecutionEvent;
import ar.edu.unsl.mys.resources.airstrips.*;
import ar.edu.unsl.mys.policies.LowestDurabilityPolicy;
//import ar.edu.unsl.mys.events.ArrivalEvent;
//import ar.edu.unsl.mys.resources.CustomQueue;
//import ar.edu.unsl.mys.events.StopExecutionEvent;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.resources.airstrips.LightAirstrip;
import ar.edu.unsl.mys.resources.airstrips.Server;
import ar.edu.unsl.mys.resources.queues.CustomQueue;
import ar.edu.unsl.mys.resources.*;

/**
 * Event oriented simulation of an airport
 */
public class AirportSimulation implements Engine
{
    private String report;
    private double endTime;
    private boolean stopSimulation;
    private FutureEventList fel;
    private List<Server> servers;

    /**
     * Creates the execution engine for the airport simulator.
     * @param airstripQuantity The number of airstrips (servers).
     * @param endTime The amount of time the simulator will simulate (run length).
     * @param policy The object that defines the airstrip selection policy 
     * each time an arrival occurs.
     */
    public AirportSimulation(int lightAirstripQuantity, int mediumAirstripQuantity, int heavyAirstripQuantity, int auxiliaryAirstripQuantity, double endTime, ServerSelectionPolicy policy, int executionTimes)
    {
        this.endTime = endTime;
        this.fel = new FutureEventList();
        this.stopSimulation = false;
        this.servers = new LinkedList<>();
        
        for(int i = 0; i < lightAirstripQuantity; i++) this.servers.add(new LightAirstrip(new CustomQueue()));
        for(int i = 0; i < mediumAirstripQuantity; i++) this.servers.add(new MediumAirstrip(new CustomQueue()));
        for(int i = 0; i < heavyAirstripQuantity; i++) this.servers.add(new HeavyAirstrip(new CustomQueue()));
        for(int i = 0; i < auxiliaryAirstripQuantity; i++) this.servers.add(new AuxiliaryAirstrip(new CustomQueue()));
        
        this.fel.insert(new StopExecutionEvent(endTime, this));
        this.fel.insert(new ArrivalEvent(0.0, new LightAircraft(), policy));
        this.fel.insert(new ArrivalEvent(0.0, new MediumAircraft(), policy));
        this.fel.insert(new ArrivalEvent(0.0, new HeavyAircraft(), policy));
        this.fel.insert(new ArrivalEvent(0.0, new Maintenance(), LowestDurabilityPolicy.getInstance()));
    }
    public List<Server> getServerList()
    {
        return this.servers;
    }

    public double getEndTime()
    {
        return this.endTime;
    }

    public void setStopSimulation(boolean stopSimulation)
    {
        this.stopSimulation = stopSimulation;
    }

    @Override
    public void execute() {
        //Incialización de variables
        while(!this.stopSimulation)
        {
            this.fel.getImminent().planificate(this.servers, this.fel);
        }
        
        //System.out.println("\nSimulation completed successfully\n");
    }

    public void addResults()
    {

    }
    @Override
    public String generateReport(boolean intoFile, String fileName) {
        return null;
    }
}