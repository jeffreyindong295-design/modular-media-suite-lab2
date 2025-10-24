import java.util.*;

// ============================================================
// ========== ADAPTER PATTERN ==========
// Supports multiple media types (Local, Stream, API)
interface Source {
    void connect(String mediaName);
}

class LocalSource implements Source {
    public void connect(String mediaName) {
        System.out.println("Opening local file: " + mediaName);
    }
}

class StreamSource implements Source {
    public void connect(String mediaName) {
        System.out.println("Accessing HLS stream: " + mediaName);
    }
}

class ApiSource implements Source {
    public void connect(String mediaName) {
        System.out.println("Requesting media from remote API: " + mediaName);
    }
}

class MediaApp {
    private Source source;
    public MediaApp(Source source) { this.source = source; }
    public void playMedia(String name) {
        System.out.println("\n[Adapter Pattern]");
        source.connect(name);
    }
}

// ============================================================
// ========== BRIDGE PATTERN ==========
// Allows switching between different rendering modes
interface Renderer {
    void render(String mediaName);
}

class HardwareRenderer implements Renderer {
    public void render(String mediaName) {
        System.out.println("Rendering " + mediaName + " with hardware acceleration.");
    }
}

class SoftwareRenderer implements Renderer {
    public void render(String mediaName) {
        System.out.println("Rendering " + mediaName + " using software rendering.");
    }
}

abstract class MediaPlayerBridge {
    protected Renderer renderer;
    public MediaPlayerBridge(Renderer renderer) { this.renderer = renderer; }
    public abstract void play(String name);
}

class AdvancedMediaPlayer extends MediaPlayerBridge {
    public AdvancedMediaPlayer(Renderer renderer) { super(renderer); }
    public void play(String name) {
        System.out.println("\n[Bridge Pattern]");
        renderer.render(name);
        System.out.println("Playing: " + name);
    }
}

// ============================================================
// ========== DECORATOR PATTERN ==========
// Adds runtime features: captions, filter, watermark
interface Player {
    void play();
}

class BaseMediaPlayer implements Player {
    private String fileName;
    private Renderer renderer;

    public BaseMediaPlayer(String fileName, Renderer renderer) {
        this.fileName = fileName;
        this.renderer = renderer;
    }

    public void play() {
        renderer.render(fileName);
        System.out.println("Playing " + fileName + "...");
    }
}

abstract class PlayerDecorator implements Player {
    protected Player wrappee;
    public PlayerDecorator(Player player) { this.wrappee = player; }
    public void play() { wrappee.play(); }
}



class SubtitleDecorator extends PlayerDecorator {
    public SubtitleDecorator(Player player) { super(player); }
    public void play() {
        super.play();
        System.out.println("Subtitles enabled.");
    }
}

class EqualizerDecorator extends PlayerDecorator {
    public EqualizerDecorator(Player player) { super(player); }
    public void play() {
        super.play();
        System.out.println("Equalizer applied.");
    }
}

class WatermarkDecorator extends PlayerDecorator {
    public WatermarkDecorator(Player player) { super(player); }
    public void play() {
        super.play();
        System.out.println("Watermark displayed.");
    }
}

class MediaController {
    private Player player;
    public MediaController(Player player) { this.player = player; }
    public void startPlay() {
        System.out.println("\n[Decorator Pattern]");
        player.play();
    }
}

// ============================================================
// ========== COMPOSITE PATTERN ==========
// Nested playlists
interface MediaItem {
    void showInfo();
}

class Track implements MediaItem {
    private String title;
    public Track(String title) { this.title = title; }
    public void showInfo() {
        System.out.println("Track: " + title);
    }
}

class Playlist implements MediaItem {
    private String title;
    private List<MediaItem> items = new ArrayList<>();

    public Playlist(String title) { this.title = title; }
    public void add(MediaItem item) { items.add(item); }

    public void showInfo() {
        System.out.println("Playlist: " + title);
        for (MediaItem i : items) {
            i.showInfo();
        }
    }
}

class PlaylistManager {
    private MediaItem root;
    public PlaylistManager(MediaItem root) { this.root = root; }
    public void showAll() {
        System.out.println("\n[Composite Pattern]");
        root.showInfo();
    }
}

// ============================================================
// ========== PROXY PATTERN ==========
// Adds caching layer for remote streaming
interface RemoteMedia {
    void playStream(String fileName);
}

class RealRemoteMedia implements RemoteMedia {
    public void playStream(String fileName) {
        System.out.println("Streaming remote media: " + fileName);
    }
}

class RemoteMediaProxy implements RemoteMedia {
    private RealRemoteMedia realMedia;
    private String cache;

    public void playStream(String fileName) {
        System.out.println("\n[Proxy Pattern]");
        if (cache == null || !cache.equals(fileName)) {
            System.out.println("Caching remote stream for: " + fileName);
            realMedia = new RealRemoteMedia();
            cache = fileName;
        } else {
            System.out.println("Using cached version for: " + fileName);
        }
        realMedia.playStream(fileName);
    }
}

class StreamController {
    private RemoteMedia stream;
    public StreamController(RemoteMedia stream) { this.stream = stream; }
    public void playStream(String name) { stream.playStream(name); }
}

// ============================================================
// ========== MAIN APPLICATION ==========
// ============================================================

public class Lab2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== Modular Media Suite =====");
        System.out.print("Enter media source (local/stream/api): ");
        String srcType = sc.nextLine();

        System.out.print("Enter media name: ");
        String media = sc.nextLine();

        // === ADAPTER PATTERN ===
        Source source;
        switch (srcType.toLowerCase()) {
            case "stream": source = new StreamSource(); break;
            case "api": source = new ApiSource(); break;
            default: source = new LocalSource(); break;
        }
        MediaApp app = new MediaApp(source);
        app.playMedia(media);

        // === BRIDGE PATTERN ===
        System.out.print("Use hardware rendering? (yes/no): ");
        Renderer renderer = sc.nextLine().equalsIgnoreCase("yes")
                ? new HardwareRenderer()
                : new SoftwareRenderer();
        AdvancedMediaPlayer bridge = new AdvancedMediaPlayer(renderer);
        bridge.play(media);

        // === DECORATOR PATTERN ===
        Player player = new BaseMediaPlayer(media, renderer);
        System.out.print("Enable subtitles? (yes/no): ");
        if (sc.nextLine().equalsIgnoreCase("yes")) player = new SubtitleDecorator(player);

        System.out.print("Enable equalizer? (yes/no): ");
        if (sc.nextLine().equalsIgnoreCase("yes")) player = new EqualizerDecorator(player);

        System.out.print("Enable watermark? (yes/no): ");
        if (sc.nextLine().equalsIgnoreCase("yes")) player = new WatermarkDecorator(player);

        MediaController controller = new MediaController(player);
        controller.startPlay();

        // === PROXY PATTERN ===
        if (srcType.equalsIgnoreCase("api")) {
            StreamController proxyCtrl = new StreamController(new RemoteMediaProxy());
            proxyCtrl.playStream(media);
        }

        // === COMPOSITE PATTERN ===
        Playlist main = new Playlist("Main Playlist");
        main.add(new Track(media));
        main.add(new Track("Bonus Track.mp3"));
        Playlist mix = new Playlist("Chill Mix");
        mix.add(new Track("TrackA.mp3"));
        mix.add(new Track("TrackB.mp3"));
        main.add(mix);

        PlaylistManager manager = new PlaylistManager(main);
        manager.showAll();

        sc.close();
        
    }
}
