# Geo Tracker

A simple geo tracker app for android

## Wat?

There are many geo tracking apps already. Why do you do this to me?

This app has a unique approach:

 * **Simplicity:** This app can't do anything. It will not show you a nice map of the locations recored
   or anything similar. It will just record a location in a predefined schedule and send it to
   server of your choice.
 * **Run your own server:** With this app, you can (and have to) run your own webservice to send the
   data to

## How to build

I don't really know. Use Android Studio and click buttons

## How to use

1. Install this [tracker service](https://github.com/sne11ius/geotracker-service) somewhere
2. Create an account on your installation - you will get an api key
3. Open app
4. Enter the Url that points to your service
5. Enter your api key
6. Set schedule
7. Enable tracking

Alternatively, you can use the service hosted @ [https://wasis.nu/mit/geotracker-service](https://wasis.nu/mit/geotracker-service).

In this case, enter `https://wasis.nu/mit/geotracker-service/coordinates` as service url.

## License

Yep, it's GPL v3 - get over with it ;)

Also: see the [`LICENSE`](https://raw.githubusercontent.com/sne11ius/GeoTracker/master/LICENSE) file
