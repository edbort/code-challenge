package bortolin.edison.husky.code.challenge.memcache;

public class MemCacheException  extends Exception {

    public MemCacheException() {
    }

    public MemCacheException(String message) {
        super(message);
    }

    public MemCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemCacheException(Throwable cause) {
        super(cause);
    }
    
}
