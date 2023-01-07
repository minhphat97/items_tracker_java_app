/**
 * A class generates multiple instances of type for object via getInstance
 *
 * @author MinhPhatTran
 */
package ca.cmpt213.a4.client.model;
public class ConsumableFactory {
    // Generate multiple instances via function getInstance
    public Consumable getInstance(String instanceType) {
        if (instanceType == null) {
            return null;
        }
        if (instanceType.equalsIgnoreCase("FOOD")) {
            return new Food();
        } else if (instanceType.equalsIgnoreCase("DRINK")) {
            return new Drink();
        }
        return null;
    }
}//ConsumableFactory.java
