package thelm.jaopca.materials;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import thelm.jaopca.api.materials.EnumMaterialType;
import thelm.jaopca.config.ConfigHandler;
import thelm.jaopca.utils.ApiImpl;

public class MaterialHandler {

	private MaterialHandler() {}

	private static final Logger LOGGER = LogManager.getLogger();
	private static final TreeMap<String, Material> MATERIALS = new TreeMap<>();
	private static Material noneMaterial;

	public static Map<String, Material> getMaterialMap() {
		return MATERIALS;
	}

	public static Collection<Material> getMaterials() {
		return MATERIALS.values();
	}

	public static Material getMaterial(String name) {
		return MATERIALS.get(name);
	}

	public static Material getNoneMaterial() {
		return noneMaterial;
	}

	public static boolean containsMaterial(String name) {
		return MATERIALS.containsKey(name);
	}

	public static void findMaterials() {
		MATERIALS.clear();

		Set<String> allMaterials = new TreeSet<>();

		Set<String> ingots = ConfigHandler.ingot ? findItemTagNamesWithPaths("forge:ingots/", "forge:ores/") : new LinkedHashSet<>();
		ingots.removeAll(ConfigHandler.GEM_OVERRIDES);
		ingots.removeAll(ConfigHandler.CRYSTAL_OVERRIDES);
		ingots.removeAll(ConfigHandler.DUST_OVERRIDES);
		allMaterials.addAll(ingots);

		Set<String> gems = ConfigHandler.gem ? findItemTagNamesWithPaths("forge:gems/", "forge:ores/") : new LinkedHashSet<>();
		gems.removeAll(allMaterials);
		gems.removeAll(ConfigHandler.CRYSTAL_OVERRIDES);
		gems.removeAll(ConfigHandler.DUST_OVERRIDES);
		allMaterials.addAll(gems);

		Set<String> crystals = ConfigHandler.crystal ? findItemTagNamesWithPaths("forge:crystals/", "forge:ores/") : new LinkedHashSet<>();
		crystals.removeAll(allMaterials);
		crystals.removeAll(ConfigHandler.DUST_OVERRIDES);
		allMaterials.addAll(crystals);

		Set<String> dusts = ConfigHandler.dust ? findItemTagNamesWithPaths("forge:dusts/", "forge:ores/") : new LinkedHashSet<>();
		dusts.removeAll(allMaterials);
		allMaterials.addAll(dusts);

		Set<String> ingotsPlain = ConfigHandler.ingotPlain ? findItemTagNamesWithPath("forge:ingots/") : new LinkedHashSet<>();
		ingotsPlain.removeAll(allMaterials);
		ingotsPlain.removeAll(ConfigHandler.GEM_OVERRIDES);
		ingotsPlain.removeAll(ConfigHandler.CRYSTAL_OVERRIDES);
		ingotsPlain.removeAll(ConfigHandler.DUST_OVERRIDES);
		allMaterials.addAll(ingotsPlain);

		Set<String> gemsPlain = ConfigHandler.gemPlain ? findItemTagNamesWithPath("forge:gems/") : new LinkedHashSet<>();
		gemsPlain.removeAll(allMaterials);
		gemsPlain.removeAll(ConfigHandler.CRYSTAL_OVERRIDES);
		gemsPlain.removeAll(ConfigHandler.DUST_OVERRIDES);
		allMaterials.addAll(gemsPlain);

		Set<String> crystalsPlain = ConfigHandler.crystalPlain ? findItemTagNamesWithPath("forge:crystals/") : new LinkedHashSet<>();
		crystalsPlain.removeAll(allMaterials);
		crystalsPlain.removeAll(ConfigHandler.DUST_OVERRIDES);
		allMaterials.addAll(crystalsPlain);

		Set<String> dustsPlain = ConfigHandler.dustPlain ? findItemTagNamesWithPath("forge:dusts/") : new LinkedHashSet<>();
		dustsPlain.removeAll(allMaterials);
		allMaterials.addAll(dustsPlain);

		noneMaterial = new Material("", EnumMaterialType.NONE);
		MATERIALS.put("", noneMaterial);
		LOGGER.debug("Added none material");
		for(String name : ingots) {
			Material material = new Material(name, EnumMaterialType.INGOT);
			MATERIALS.put(name, material);
			LOGGER.debug("Added ingot material {}", name);
		}
		for(String name : gems) {
			Material material = new Material(name, EnumMaterialType.GEM);
			MATERIALS.put(name, material);
			LOGGER.debug("Added gem material {}", name);
		}
		for(String name : crystals) {
			Material material = new Material(name, EnumMaterialType.CRYSTAL);
			MATERIALS.put(name, material);
			LOGGER.debug("Added crystal material {}", name);
		}
		for(String name : dusts) {
			Material material = new Material(name, EnumMaterialType.DUST);
			MATERIALS.put(name, material);
			LOGGER.debug("Added dust material {}", name);
		}
		for(String name : ingotsPlain) {
			Material material = new Material(name, EnumMaterialType.INGOT_PLAIN);
			MATERIALS.put(name, material);
			LOGGER.debug("Added plain ingot material {}", name);
		}
		for(String name : gemsPlain) {
			Material material = new Material(name, EnumMaterialType.GEM_PLAIN);
			MATERIALS.put(name, material);
			LOGGER.debug("Added plain gem material {}", name);
		}
		for(String name : crystalsPlain) {
			Material material = new Material(name, EnumMaterialType.CRYSTAL_PLAIN);
			MATERIALS.put(name, material);
			LOGGER.debug("Added plain crystal material {}", name);
		}
		for(String name : dustsPlain) {
			Material material = new Material(name, EnumMaterialType.DUST_PLAIN);
			MATERIALS.put(name, material);
			LOGGER.debug("Added plain dust material {}", name);
		}
		LOGGER.info("Added {} materials", MATERIALS.size());
	}

	protected static Set<String> findItemTagNamesWithPath(String path) {
		Set<String> ret = new TreeSet<>();
		Set<String> tags = ApiImpl.INSTANCE.getItemTags().stream().map(ResourceLocation::toString).collect(Collectors.toCollection(LinkedHashSet::new));
		for(String tag : tags) {
			if(tag.startsWith(path)) {
				String name = tag.substring(path.length());
				ret.add(name);
			}
		}
		return ret;
	}

	protected static Set<String> findItemTagNamesWithPaths(String path1, String path2) {
		Set<String> ret = new TreeSet<>();
		Set<String> tags = ApiImpl.INSTANCE.getItemTags().stream().map(ResourceLocation::toString).collect(Collectors.toCollection(LinkedHashSet::new));
		for(String tag : tags) {
			if(tag.startsWith(path1)) {
				String name = tag.substring(path1.length());
				if(tags.contains(path2+name)) {
					ret.add(name);
				}
			}
		}
		return ret;
	}
}
