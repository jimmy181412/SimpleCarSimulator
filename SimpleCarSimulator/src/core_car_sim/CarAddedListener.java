package core_car_sim;

public interface CarAddedListener {
	AbstractCar createCar(String name, Point startingLoca, Point endingLoca);
	AbstractCar createCar(String name, Point startingLoca, Point endingLoca, String av);
}