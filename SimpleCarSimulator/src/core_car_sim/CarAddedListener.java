package core_car_sim;

public interface CarAddedListener {
	AbstractCar createCar(String name, Point startingLoca, Point endingLoca, Point referenceLoca);
	AbstractCar createCar(String name, Point startingLoca, Point endingLoca,Point referenceLoca, String av);
}