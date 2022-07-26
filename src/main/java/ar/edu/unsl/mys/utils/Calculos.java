package ar.edu.unsl.mys.utils;

import ar.edu.unsl.mys.resources.airstrips.*;
import ar.edu.unsl.mys.App;
import ar.edu.unsl.mys.entities.aircrafts.*;
import java.util.*;
import java.io.*;

public class Calculos {

    private static int CANT_ATRIBUTOS = 36;
    private static float CERTEZA = 0.95f;
    private static float PROBABILIDAD_CERTEZA = 1.96f;

    private double[][] ejecuciones;
    private double[] calculos;
    private double[] sigma;
    private double[][] intervalos;
    private String report;
    private int repeticiones;

    public Calculos(int repeticiones)
    {
        this.repeticiones = repeticiones;
        ejecuciones = new double[repeticiones][CANT_ATRIBUTOS];
        calculos = new double[CANT_ATRIBUTOS];
        sigma = new double[CANT_ATRIBUTOS];
        intervalos = new double[CANT_ATRIBUTOS][2];
        for(int i = 0; i < CANT_ATRIBUTOS; i++)
        {
            calculos[i] = 0.0;
            sigma[i] = 0.0;
        }
    }

    public void save(int execution, List<Server> servers)
    {
    /*
        Posición 0: cantidad media de aeronaves que aterrizaron
        Posición 1: tiempo medio de espera en cola
        Posición 2: tiempo máximo de espera en cola
        Posición 3: tiempo medio de tránsito
        Posición 4: tiempo máximo de tránsito
		
		Posición 5: cantidad media de aeronaves que aterrizaron (livianas)
        Posición 6: tiempo medio de espera en cola (livianas)
        Posición 7: tiempo máximo de espera en cola (livianas)
        Posición 8: tiempo medio de tránsito (livianas)
        Posición 9: tiempo máximo de tránsito (livianas)

		Posición 10: cantidad media de aeronaves que aterrizaron (medianas)
        Posición 11: tiempo medio de espera en cola (medianas)
        Posición 12: tiempo máximo de espera en cola (medianas)
        Posición 13: tiempo medio de tránsito (medianas)
        Posición 14: tiempo máximo de tránsito (medianas)

		Posición 15: cantidad media de aeronaves que aterrizaron (pesadas)
        Posición 16: tiempo medio de espera en cola (pesadas)
        Posición 17: tiempo máximo de espera en cola (pesadas)
        Posición 18: tiempo medio de tránsito (pesadas)
        Posición 19: tiempo máximo de tránsito (pesadas)

        Posición 20: tiempo medio total de ocio de la pista 1 
        Posición 21: tiempo medio total de ocio de la pista 2 
        Posición 22: tiempo medio total de ocio de la pista 3 
        Posición 23: tiempo medio total de ocio de la pista 4 
        
        Posición 24: tiempo máximo de ocio de la pista 1 
        Posición 25: tiempo máximo de ocio de la pista 2 
        Posición 26: tiempo máximo de ocio de la pista 3 
        Posición 27: tiempo máximo de ocio de la pista 4 
        
        Posición 28: tamaño máximo de la cola de espera para la pista 1
        Posición 29: tamaño máximo de la cola de espera para la pista 2
        Posición 30: tamaño máximo de la cola de espera para la pista 3
        Posición 31: tamaño máximo de la cola de espera para la pista 4

        Posición 32: durabilidad del sueño restante al finalizar la simulación de la pista 1
        Posición 33: durabilidad del sueño restante al finalizar la simulación de la pista 2
        Posición 34: durabilidad del sueño restante al finalizar la simulación de la pista 3
        Posición 35: durabilidad del sueño restante al finalizar la simulación de la pista 4
    */

        ejecuciones[execution][0] = (double)Aircraft.getProcessedAmount();
        ejecuciones[execution][1] = Aircraft.getTotalWaitingTime()/Aircraft.getWaitingCount();
        ejecuciones[execution][2] = Aircraft.getMaxWaitingTime();
        ejecuciones[execution][3] = Aircraft.getTotalTransitTime()/Aircraft.getProcessedAmount();
        ejecuciones[execution][4] = Aircraft.getMaxTransitTime();
        
        ejecuciones[execution][5] = (double)LightAircraft.getProcessedAmount();
        ejecuciones[execution][6] = LightAircraft.getTotalWaitingTime()/LightAircraft.getWaitingCount();
        ejecuciones[execution][7] = LightAircraft.getMaxWaitingTime();
        ejecuciones[execution][8] = LightAircraft.getTotalTransitTime()/LightAircraft.getProcessedAmount();
        ejecuciones[execution][9] = LightAircraft.getMaxTransitTime();
       
        ejecuciones[execution][10] = (double)MediumAircraft.getProcessedAmount();
        ejecuciones[execution][11] = MediumAircraft.getTotalWaitingTime()/MediumAircraft.getWaitingCount();
        ejecuciones[execution][12] = MediumAircraft.getMaxWaitingTime();
        ejecuciones[execution][13] = MediumAircraft.getTotalTransitTime()/MediumAircraft.getProcessedAmount();
        ejecuciones[execution][14] = MediumAircraft.getMaxTransitTime();
        
        ejecuciones[execution][15] = (double)HeavyAircraft.getProcessedAmount();
        ejecuciones[execution][16] = HeavyAircraft.getTotalWaitingTime()/HeavyAircraft.getWaitingCount();
        ejecuciones[execution][17] = HeavyAircraft.getMaxWaitingTime();
        ejecuciones[execution][18] = HeavyAircraft.getTotalTransitTime()/HeavyAircraft.getProcessedAmount();
        ejecuciones[execution][19] = HeavyAircraft.getMaxTransitTime();
        
        ejecuciones[execution][20] = servers.get(0).getIdleTime();
        ejecuciones[execution][21] = servers.get(1).getIdleTime();
        ejecuciones[execution][22] = servers.get(2).getIdleTime();
        ejecuciones[execution][23] = servers.get(3).getIdleTime();
        
        ejecuciones[execution][24] = servers.get(0).getMaxIdleTime();
        ejecuciones[execution][25] = servers.get(1).getMaxIdleTime();
        ejecuciones[execution][26] = servers.get(2).getMaxIdleTime();
        ejecuciones[execution][27] = servers.get(3).getMaxIdleTime();
        
        ejecuciones[execution][28] = (double)servers.get(0).getWQueue().getMaxSize();
        ejecuciones[execution][29] = (double)servers.get(1).getWQueue().getMaxSize();
        ejecuciones[execution][30] = (double)servers.get(2).getWQueue().getMaxSize();
        ejecuciones[execution][31] = (double)servers.get(3).getWQueue().getMaxSize();
       
        ejecuciones[execution][32] = (double)((Airstrip)(servers.get(0))).getSurfaceDurability();
        ejecuciones[execution][33] = (double)((Airstrip)(servers.get(1))).getSurfaceDurability();
        ejecuciones[execution][34] = (double)((Airstrip)(servers.get(2))).getSurfaceDurability();
        ejecuciones[execution][35] = (double)((Airstrip)(servers.get(3))).getSurfaceDurability();

    }

    public void calcular()
    {

        for(int i = 0; i < CANT_ATRIBUTOS; i++)
        {
            for(int j = 0; j < this.repeticiones; j++)
            {
                calculos[i] += ejecuciones[j][i];
            }
        }

        for(int i = 0; i < CANT_ATRIBUTOS; i++)
        {
            calculos[i] = calculos[i]/50;
        }

        for(int i = 0; i < CANT_ATRIBUTOS; i++)
        {
            for(int j = 0; j < this.repeticiones; j++)
            {
                sigma[i] += Math.pow(calculos[i]-ejecuciones[j][i], 2.0);
            }
            sigma[i] = Math.sqrt(sigma[i]/(this.repeticiones*(this.repeticiones-1)));
        } 

        for(int i = 0; i < CANT_ATRIBUTOS; i++)
        {
            intervalos[i][0] = calculos[i] - ((double)PROBABILIDAD_CERTEZA * (sigma[i]/Math.sqrt(this.repeticiones)));
            intervalos[i][1] = calculos[i] + ((double)PROBABILIDAD_CERTEZA * (sigma[i]/Math.sqrt(this.repeticiones)));
        }
    }

    public void imprimir()
    {
        this.report = "---------------------------------------------------------------\n" +
                      "                     Reporte (sin estimación)                  \n" +
                 "---------------------------------------------------------------\n" +
                 "________________________Aviones General________________________\n" +
                 "Cantidad media de aeronaves que aterrizaron: (" + intervalos[0][0] + " ; " + intervalos[0][1] + ")" + "\n" +
                 "Tiempo medio de espera en cola: (" + intervalos[1][0] + " ; " + intervalos[1][1] + ")" + "\n" +
                 "Media del tiempo máximo de espera en cola: (" + intervalos[2][0] + " ; " + intervalos[2][1] + ")" + "\n" +
                 "Tiempo medio de tránsito: (" + intervalos[3][0] + " ; " + intervalos[3][1] + ")" + "\n" +
                 "Media del tiempo máximo de tránsito: (" + intervalos[4][0] + " ; " + intervalos[4][1] + ")" + "\n\n" +
                 "_______________________Aviones Livianos________________________\n" +         
                 "Cantidad media de aeronaves que aterrizaron: (" + intervalos[5][0] + " ; " + intervalos[5][1] + ")" +"\n" +
                 "Tiempo medio de espera en cola: (" + intervalos[6][0] + " ; " + intervalos[6][1] + ")" + "\n" +
                 "Media del tiempo máximo de espera en cola: (" + intervalos[7][0] + " ; " + intervalos[7][1] + ")" + "\n" +
                 "Tiempo medio de tránsito: (" + intervalos[8][0] + " ; " + intervalos[8][1] + ")" + "\n" + 
                 "Media del tiempo máximo de tránsito: (" + intervalos[9][0] + " ; " + intervalos[9][1] + ")" + "\n\n" +
                 "_______________________Aviones Medianos________________________\n" +         
                 "Cantidad media de aeronaves que aterrizaron: (" + intervalos[10][0] + " ; " + intervalos[10][1] + ")" +"\n" +
                 "Tiempo medio de espera en cola: (" + intervalos[11][0] + " ; " + intervalos[11][1] + ")" + "\n" +
                 "Media del tiempo máximo de espera en cola: (" + intervalos[12][0] + " ; " + intervalos[12][1] + ")" + "\n" +
                 "Tiempo medio de tránsito: (" + intervalos[13][0] + " ; " + intervalos[13][1] + ")" + "\n" + 
                 "Media del tiempo máximo de tránsito: (" + intervalos[14][0] + " ; " + intervalos[14][1] + ")" + "\n\n" +
                 "_______________________Aviones Pesados________________________\n"  +        
                 "Cantidad media de aeronaves que aterrizaron: (" + intervalos[15][0] + " ; " + intervalos[15][1] + ")" +"\n" +
                 "Tiempo medio de espera en cola: (" + intervalos[16][0] + " ; " + intervalos[16][1] + ")" + "\n" +
                 "Media del tiempo máximo de espera en cola: (" + intervalos[17][0] + " ; " + intervalos[17][1] + ")" + "\n" +
                 "Tiempo medio de tránsito: (" + intervalos[18][0] + " ; " + intervalos[18][1] + ")" + "\n" + 
                 "Media del tiempo máximo de tránsito: " + calculos[19] + "\n\n" +
                 "______________________Pista 1 (mediana)_______________________\n" +
                 "Tiempo medio total de ocio: (" + intervalos[20][0] + " ; " + intervalos[20][1] + ")" + "\n" +
                 "Porcentaje del tiempo medio\nde ocio de la pista respecto al tiempo total: " + (calculos[20]*100)/App.getExecutionTime() + "%\n" +
                 "Media de tiempo máximo de ocio: (" + intervalos[24][0] + " ; " + intervalos[24][1] + ")" + "\n" +
                 "Porcentaje del tiempo máximo\nde ocio de la pista respecto al\ntiempo total de ocio: " + (calculos[24]*100)/calculos[20] + "%\n" +
                 "Media del tamaño máximo de la cola de espera: (" + intervalos[28][0] + " ; " + intervalos[28][1] + ")" + "\n" +
                 "Media de la durabilidad del suelo\nrestante al finalizar la simulación: (" + intervalos[32][0] + " ; " + intervalos[2][1] + ")" + "\n\n" +
                 "______________________Pista 2 (mediana)_______________________\n" +
                 "Tiempo medio total de ocio: (" + intervalos[21][0] + " ; " + intervalos[21][1] + ")" + "\n" +
                 "Porcentaje del tiempo medio\nde ocio de la pista respecto al tiempo total: " + (calculos[21]*100)/App.getExecutionTime() + "%\n" +
                 "Media de tiempo máximo de ocio: (" + intervalos[25][0] + " ; " + intervalos[25][1] + ")" + "\n" +
                 "Porcentaje del tiempo máximo\nde ocio de la pista respecto\nal tiempo total de ocio: " + (calculos[25]*100)/calculos[21] + "%\n" +
                 "Media del tamaño máximo de la cola de espera: (" + intervalos[29][0] + " ; " + intervalos[29][1] + ")" + "\n" +
                 "Media de la durabilidad del suelo\nrestante al finalizar la simulación: (" + intervalos[33][0] + " ; " + intervalos[33][1] + ")" + "\n\n" +
                 "______________________Pista 3 (mediana)_______________________\n" +
                 "Tiempo medio total de ocio: (" + intervalos[22][0] + " ; " + intervalos[22][1] + ")" + "\n" +
                 "Porcentaje del tiempo medio\nde ocio de la pista respecto al tiempo total: " + (calculos[22]*100)/App.getExecutionTime() + "%\n" +
                 "Media de tiempo máximo de ocio: (" + intervalos[26][0] + " ; " + intervalos[26][1] + ")" + "\n" +
                 "Porcentaje del tiempo máximo\nde ocio de la pista respecto\nal tiempo total de ocio: " + (calculos[26]*100)/calculos[22] + "%\n" +
                 "Media del tamaño máximo de la cola de espera: (" + intervalos[30][0] + " ; " + intervalos[30][1] + ")" + "\n" +
                 "Media de la durabilidad del suelo\nrestante al finalizar la simulación: (" + intervalos[34][0] + " ; " + intervalos[34][1] + ")" + "\n\n" +                
                 "______________________Pista 4 (mediana)_______________________\n" +
                 "Tiempo medio total de ocio: (" + intervalos[23][0] + " ; " + intervalos[23][1] + ")" + "\n" +
                 "Porcentaje del tiempo medio\nde ocio de la pista respecto al tiempo total: " + (calculos[23]*100)/App.getExecutionTime() + "%\n" +
                 "Media de tiempo máximo de ocio: (" + intervalos[27][0] + " ; " + intervalos[27][1] + ")" + "\n" +
                 "Porcentaje del tiempo máximo\nde ocio de la pista respecto\nal tiempo total de ocio: " + (calculos[27]*100)/calculos[23] + "%\n" +
                 "Media del tamaño máximo de la cola de espera: (" + intervalos[31][0] + " ; " + intervalos[31][1] + ")" + "\n" +
                 "Media de la durabilidad del suelo\nrestante al finalizar la simulación: (" + intervalos[35][0] + " ; " + intervalos[35][1] + ")" + "\n\n";
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Report.txt"));
            writer.write(this.report);
            writer.close();
        }
        catch (Exception exception)
        {
            System.out.println("Error when trying to write the report into a file.");
            System.out.println("Showing on screen...");
        }
    
        System.out.println(this.report);
    }
}
