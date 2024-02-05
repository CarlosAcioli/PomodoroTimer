# Pomodoro 🍅

Application to track your production time following the Pomodoro technique. <br>
You can also use customs focus and pause time! Flexible to what you want

## Future features 

There will still be implemented of a feature to see the user history. When each item will have:

- Name: the name of the full Pomodoro cycle that the user did
- Description: description to the user writes what he did and be more specific if want
- Times used: what times the user used in focus and pause durations
- Date created: the date the user did the Pomodoro

It's going to work similarly as a to-do app, so the user can see all of his history to see what he did on the last days, how much time, how often, and take your own conclusions

## How is the app structured? 👷

Basically the app was developed using the MVVM + Clean Architecture patterns, to provide a best maintainability and to facilitate the addition of new features on the future

## Technologies used in the app ⚙️🧰
>How it works behind the scenes?  

- [Duration API](https://kotlinlang.org/docs/time-measurement.html) - Used to track the timer by minutes, it has a lot of features, transform different time units: minutes to seconds, hours to seconds, hours to minutes, seconds in Long DataType. And can also store the total time in an ISO, keeping it easy to save in databases.

- [Kotlin Coroutines Flow](https://kotlinlang.org/docs/time-measurement.html) - To handle the countdown timer, decreasing the time seconds by seconds from the time chosed. Launched in a coroutine dispatcher to deal with timer asynchronously, without interrupting the main thread

- [Android Services](https://developer.android.com/develop/background-work/services) - Used to make the timer still works even if the app is closed and destroyed. The service keeps running in the background and showing it to the user, signaling that the app is still working if he closes the app with no intention

- [Notifications](https://developer.android.com/develop/ui/views/notifications) - Generally used with the service, to show the user that the apps still running in the background, and updates the notification showing the current time. And if clicked, the app is opened and with the same data of showing at the notification

## App screenshots

![image](https://github.com/CarlosAcioli/PomodoroTimer/assets/131110961/29cbd1dd-5b6a-4907-a177-009ab74504c3) <br>
![image](https://github.com/CarlosAcioli/PomodoroTimer/assets/131110961/f9915ca9-c8c0-4f19-9a2c-c6343f84bf5c) <br>
![image](https://github.com/CarlosAcioli/PomodoroTimer/assets/131110961/cf2454b7-763c-4c4f-9f70-77a2f904019d)



