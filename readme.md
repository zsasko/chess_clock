# Chess Clock

Application that displays chess clock where user can select different rulesets and create its own ruleset.

![](https://raw.githubusercontent.com/zsasko/android-mvi-sample/main/assets/combined_centered.png)


# Features
- **Chess Controller**
    - Controller that handles all the business logic i.e. how timer is working and when player timer should be switched to another player
        - [ChessController.kt](https://github.com/zsasko/chess_clock/blob/main/app/src/main/java/com/zsasko/chessclock/controllers/ChessController.kt)

- **Chess View Model**
    - Invokes appropriate methods from Chess Controller. All the business logic is in controller so this view model class can contain some other logic for the future, for example analytics logging. 
        - [ChessViewModel.kt](https://github.com/zsasko/chess_clock/blob/main/app/src/main/java/com/zsasko/chessclock/viewmodel/ChessViewModel.kt)

- **Dialogs**
    - Dialogs for selecting active ruleset, for creating new ruleset and generic dialog for displaying finish game message:
        - [CreateRulesetDialog.kt](https://github.com/zsasko/chess_clock/blob/main/app/src/main/java/com/zsasko/chessclock/ui/dialogs/CreateRulesetDialog.kt)
        - [GenericTextDialog.kt](https://github.com/zsasko/chess_clock/blob/main/app/src/main/java/com/zsasko/chessclock/ui/dialogs/GenericTextDialog.kt)
        - [SelectRulesetDialog.kt](https://github.com/zsasko/chess_clock/blob/main/app/src/main/java/com/zsasko/chessclock/ui/dialogs/SelectRulesetDialog.kt)

- **Custom Views**
    - Custom views for displaying clocks and clock controls.
        - [Clock.kt](https://github.com/zsasko/chess_clock/blob/main/app/src/main/java/com/zsasko/chessclock/ui/views/Clock.kt)
        - [ClockControls.kt](https://github.com/zsasko/chess_clock/blob/main/app/src/main/java/com/zsasko/chessclock/ui/views/ClockControls.kt)

# Testing

- **Instrumented Tests**
    - Instrumented test for checking will click on 'Reset' button reset ui.
        - [ChessClockResetTest.kt](https://github.com/zsasko/chess_clock/blob/main/app/src/androidTest/java/com/zsasko/chessclock/ChessClockResetTest.kt)

- **Unit Tests**
    - Test for some chess controller methods:
        - [ChessControllerImplTest.kt](https://github.com/zsasko/chess_clock/blob/main/app/src/test/java/com/zsasko/chessclock/ChessControllerTest.kt)

