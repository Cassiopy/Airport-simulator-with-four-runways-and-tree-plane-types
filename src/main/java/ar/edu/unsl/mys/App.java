
//Sofía Flores - Ingeniería en Informática

package ar.edu.unsl.mys;

import java.util.LinkedList;

import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.engine.Engine;
import ar.edu.unsl.mys.entities.aircrafts.Aircraft;
import ar.edu.unsl.mys.entities.aircrafts.HeavyAircraft;
import ar.edu.unsl.mys.entities.aircrafts.LightAircraft;
import ar.edu.unsl.mys.entities.aircrafts.MediumAircraft;
//import ar.edu.unsl.mys.policies.OneServerModelPolicy;
import ar.edu.unsl.mys.policies.ShorterQueuePolicy;
import java.util.*;
import ar.edu.unsl.mys.resources.airstrips.*;
import ar.edu.unsl.mys.resources.*;
import ar.edu.unsl.mys.utils.*;

public class App 
{
    private static final int MIN_PER_DAY = 1440;
    private static final int NUMBER_OF_DAYS = 28;
    private static final int EXECUTION_TIME = MIN_PER_DAY*NUMBER_OF_DAYS;

    private static final int LIGHTSERVERS_NUMBER = 0;
    private static final int MEDIUMSERVERS_NUMBER = 4;
    private static final int HEAVYSERVERS_NUMBER = 0;
    private static final int AUXILIARSERVERS_NUMBER = 1;

    private static final int EXECUTION_TIMES = 50;
    

    public static double getExecutionTime()
    {
        return (double)EXECUTION_TIME;
    }

    public static int getExecutionTimes()
    {
        return EXECUTION_TIMES;
    }

    public static void main( String[] args )
    {
        Calculos calculadora = new Calculos(EXECUTION_TIMES);
        Engine engine;
        List<Server> servers;

        for(int i = 0; i < EXECUTION_TIMES; i++) 
        {
            engine = new AirportSimulation(LIGHTSERVERS_NUMBER, MEDIUMSERVERS_NUMBER, HEAVYSERVERS_NUMBER, AUXILIARSERVERS_NUMBER, EXECUTION_TIME, ShorterQueuePolicy.getInstance(), EXECUTION_TIMES);
            engine.execute();
            servers = ((AirportSimulation)engine).getServerList();
            calculadora.save(i, servers);
        }
        calculadora.calcular();
        calculadora.imprimir();
    }
}