# Music_Project

REPORT
Start : 01/08/2022
Finish : 15:30 14/08/2022 (for sumbit mock project)

Main Content: 
Support SDK : 21 - 31
Music project to load media( audio, artist, album, playlist, gerne ) from device 

Function: 
+ load all song
+ add playlists and add songs to playlists 
+ watch albums and play songs of the album
+ watch artist : number of album and song
+ watch genre and number of songs of a gener
+ playing song: 
  - repeat song
  - play songs in playlist (a bit bug, howere it works - not crash app)
  - shuffle songs(a bit bugs in the part, howere it works - not crash app)
  - add current song to playlist
  - sleep timer function
+ watch recently played songs

User Interface: 
+ follow requirement UI

Structure: 
+ MVVM architecture
+ Observe pattern
+ Singleton pattern

Support Library and Support Component:
+ Databinding
+ LiveData
+ ViewModel
+ RoomDatabase
+ WorkManager
+ Dragger-Hilt
+ Media (support media player)
+ Navigation Component
+ Swipe Refresh Layout

Code flow: 
Using Observer pattern implement into MusicController which controll all task playing song and related function at MusicService
And then use a viewmodel (MainViewModel) as observe. So from the viewmodel that all components can access to MusicController to perfom their task.

Learn: 
+ folder arrangement
+ write style, string, dimes, font, menu resoure. Although they still have some confusion in their arrangement, I will improve in next time
+ get clear dragger-hilt 

Result: 
+ The app still exits some bug, show I leared a lot thing from this project. 
+ Due to the limitation of coding level and time, the project has not been completed as required

REPORTER: Nguyễn Doãn Hùng
Thank you for your reading
Have a good day!!
