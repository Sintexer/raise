package com.ilyabuglakov.raise.model.service.path;

/**
 * The type Path service.
 */
public class PathService {
    private final String FULL_PATH;

    private PathService() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String RESOURCE_PATH = "config";
        FULL_PATH = classLoader.getResource(RESOURCE_PATH).getPath();
    }

    private static class InstanceHolder {
        /**
         * The constant INSTANCE.
         */
        public static final PathService INSTANCE = new PathService();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static PathService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Gets resource path.
     *
     * @param fileName the file name
     * @return the resource path
     */
    public String getResourcePath(String fileName) {
        return FULL_PATH + "/" + fileName;
    }


}
