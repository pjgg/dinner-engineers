# The Engineers' Dinner

This project is about finding an optimal date for dinner among several engineers. Participants must create a PR to this repository and add a file with their name in 'src/test/resources/availability' (if it doesn't already exist). The format of that file is as follows:

JSON

```json
[ "dd/MM/yyyy - HH:mm | HH:mm" ]
```

For example, a valid format would be: 
```json
[ 
  "31/03/2025 - 19:00 | 21:00",
  "01/04/2025 - 19:00 | 21:00", 
  "02/04/2025 - 19:00 | 21:00"
]
```
where the dates represent the available days for dinner and the times are the available hours within that day.

Once the PR is accepted and merged, a job will run every night, and the selected date will be published on https://pjgg.github.io/dinner-engineers/ website.