package core_car_sim;

//create pedestrians
public interface PedestrianAddedListener {
	Pedestrian createPedestrians(String name, Point startingLoca, Point endingLoca, Point referenceLoca, Direction d);
}
