/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ciro
 */
public class EMF {
    // Esse nome "CursinhoPU" deve ser substituído por "SeuProjetoPU"
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("CursinhoPU");
    
    public static EntityManagerFactory getEntityManagerFactory(){
        return emf;
    }
}
