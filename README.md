# Detect Eye
The goal of the project is to:
1. Facilitate a person to navigate within confined spaces (like rooms in houses or hostel) without bumping into common objects like furniture, walls etc. In the application we intend to help people to be able to walk in such places with confidence.
2. To be accessible for all diverse audience is one of the key goals.

![Detect Eye project ](./doc/images/project.png)


## Details
This project was created as part of a I-Stem Hackathon 2018 initiative over 24hours during 20 Jan - 21 Jan 2018 done by Venkatesh Potluri, Pratyush Kaushal, Palash Gupta, Sai Krishna Mundrati & Anoob Backer. 

To begin with we focused on making it easy for visually-impared users to navigate. 

Today most of the people use normal cane, smart Cane, non-real time applications & use sighted assistance. We do understand that none of these can be replaced immediatly. 

## Android application
This project uses straightforward samples of using TensorFlow for mobile applications. 

We've leveraged the TensorFlow example - [TF Detect](https://github.com/tensorflow/tensorflow/blob/master/tensorflow/examples/android/src/org/tensorflow/demo/DetectorActivity.java) and added support for text to voice & vibration support.

Our approach is to leverage machine learning to identify the objects real time to provide multi-mode feedback to users.

![Architecture of the project](./doc/images/arch.png)

## Future
Long term: The technology improvements in the autonomous cars & medical domain is phenomenal which will make the sensors & object recognition even more better given far better latencies. All these upcoming tech will make sensors, algos, compute, storage & batter a lot more more affordable & cheaper. The app here can ported into a cane or even be part of a body as an implant provided no legal or compliance restrictions.

Short term: This can be expanded to support a lot more use cases. We would like to leverage depth sensors, customer vibrations, detection of object which are harmful or large/small etc. & further expand to much more better feedback systems.

We're hosting this online for further collaboration & making it a better. 