 Laboratory 2: Structural Design Patterns

 

Steps of Running the Program

cd src
javac Lab2.java
java Lab2

How to Test / Run Demo

When you run the program, you’ll be asked for inputs.
Enter media source (local/stream/api): api
Enter media name: SongDemo.mp3
Use hardware rendering? (yes/no): yes
Enable subtitles? (yes/no): yes
Enable equalizer? (yes/no): no
Enable watermark? (yes/no): yes

Expected Behavior:

Connects to chosen media source (Adapter pattern)

Switches renderer (Bridge pattern)

Adds features dynamically (Decorator pattern)

Shows nested playlists (Composite pattern)

Uses caching for remote streams (Proxy pattern)





