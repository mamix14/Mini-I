<p align="center">
  <img src="assets/uwb_logo.png" alt="Uniwersytet w Białymstoku" width="120"/>
</p>

# Zbiory Julii – Wizualizacja i Animacja

<p align="center">
  Projekt Mini I &nbsp;|&nbsp; Uniwersytet w Białymstoku &nbsp;|&nbsp; Wydział Informatyki
</p>

---

## O projekcie

Interaktywna aplikacja do wizualizacji i animacji **zbiorów Julii** – fraktali na płaszczyźnie zespolonej.

Zbiór Julii powstaje przez iterację wzoru:

```
z = z² + c
```

gdzie `z` i `c` są liczbami zespolonymi. Kształt fraktala zmienia się w zależności od parametru `c`.

---

## Co robi aplikacja

- Rysuje fraktal zbioru Julii w czasie rzeczywistym
- Animuje płynną zmianę parametru `c` po krzywej parametrycznej (okrąg)
- Pokazuje krzywą parametryczną `x(t) = R·cos(t)`, `y(t) = R·sin(t)` z regulacją liczby punktów
- Umożliwia ręczną zmianę parametrów przez użytkownika

---

## Funkcje

| Funkcja | Opis |
|---|---|
| ▶ Start / ⏸ Stop | Uruchamia / zatrzymuje animację |
| ↺ Reset | Wraca do wartości domyślnych |
| Re(c) / Im(c) | Ręczne ustawienie parametru c |
| Suwak iteracji | Regulacja szczegółowości (10–500) |
| Suwak punktów krzywej | Dokładność krzywej parametrycznej (4–200) |

---

## Technologia

- **Język:** Java (JDK 8+)
- **GUI:** Java Swing
- **Renderowanie:** BufferedImage + wielowątkowość (ExecutorService)
- **Brak zewnętrznych bibliotek** – tylko standardowe JDK

---

## Struktura projektu

```
src/juliaset/
├── Main.java                  # Punkt startowy aplikacji
├── JuliaSetFrame.java         # Okno główne i panel sterowania
├── JuliaSetPanel.java         # Algorytm i rendering fraktala
├── AnimationController.java   # Kontroler animacji
└── ParametricCurvePanel.java  # Wizualizacja krzywej parametrycznej
```

---

## Uruchomienie

```bash
javac -d out src/juliaset/*.java
java -cp out juliaset.Main
```

lub:

```bash
bash compile_and_run.sh
```

---

## Autor

| | |
|---|---|
| **Student** | mamix14 |
| **Przedmiot** | Wizualizacja i Animacja Struktur Algorytmicznych w 2D/3D |
| **Uczelnia** | Uniwersytet w Białymstoku |
| **Projekt** | Mini I |
