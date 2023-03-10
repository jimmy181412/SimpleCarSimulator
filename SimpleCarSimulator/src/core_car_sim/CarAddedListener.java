package core_car_sim;

public interface CarAddedListener {
	AbstractCar createCar(String name, Point startingLoca, Point endingLoca, Point referenceLoca, Direction d);
	AbstractCar createCar(String name, Point startingLoca, Point endingLoca,Point referenceLoca, Direction d, String av);
}