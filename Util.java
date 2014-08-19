
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

@SerializableAs("SphericalCuboid")
public class SphericalCuboid implements ConfigurationSerializable {
	private Vector center;
	private double rad;
	private String world;

	public SphericalCuboid(String world, Vector center, double rad) {
		this.world = world;
		this.center = center;
		this.rad = rad;
	}

	public void setCenter(Vector v) {
		this.center = v;
	}

	public void setRadius(double rad) {
		this.rad = rad;
	}

	public Vector getCenter() {
		return center;
	}

	public double getRadius() {
		return rad;
	}

	public static SphericalCuboid deserialize(Map<String, Object> map) {
		try {
			SphericalCuboid cb = new SphericalCuboid(map.get("world")
					.toString(), null, 0);
			cb.center = new Vector(((Number) map.get("x")).intValue(),
					((Number) map.get("y")).intValue(),
					((Number) map.get("z")).intValue());
			cb.rad = ((Number) map.get("radius")).doubleValue();
			return cb;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("world", world);
		map.put("x", center.getBlockX());
		map.put("y", center.getBlockY());
		map.put("z", center.getBlockZ());
		map.put("radius", rad);
		return map;
	}

	public Location getSpawnableLocation(Random rand, World w) {
		double ang = rand.nextDouble() * Math.PI * 2.0;
		double x = center.getBlockX() + (Math.cos(ang) * rad);
		double z = center.getBlockZ() + (Math.sin(ang) * rad);
		int y;
		for (y = center.getBlockY() - 5; y <= center.getBlockY() + rad; y++) {
			if (w.getBlockAt((int) x, (int) y, (int) z).getType() == Material.AIR
					&& w.getBlockAt((int) x, (int) y + 1, (int) z).getType() == Material.AIR) {
				return new Location(w, (int) x, (int) y, (int) z);
			}
			if (y == center.getBlockY() + (int) rad) {
				y = center.getBlockY() - (int) rad;
			}
		}
		return new Location(w, center.getBlockX(), center.getBlockY(),
				center.getBlockZ());
	}

	public void setWorld(String name) {
		this.world = name;
	}

	public String getWorld() {
		return world;
	}

	public boolean contains(Location location) {
		return location.toVector().distance(center) <= rad;
	}
}