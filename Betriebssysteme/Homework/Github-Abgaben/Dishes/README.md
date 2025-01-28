## EDF (Earliest Deadline First) Scheduler Implementierung für dishes.py

Bei der Implementierung unseres Schedulers haben wir uns für einen EDF-Scheduler entschieden, der das Tier, dass zuerst den Zustand `STARVING` erreicht, auch zuerst zum Fressen schickt.
Die Einschränkung besteht jedoch darin, dass entweder nur Katzen oder nur Hunde und Mäuse gleichzeitig zum Fressen geschickt werden können.

Um dies zu realisieren, haben wir uns 2 Listen angelegt:
```python
present_dogs_mouse = []
present_cats = []
```


In diesen Listen werden jeweils die Namen der Tiere gespeichert, die aktuell auf den nächsten freien Fressnapf warten oder bereits essen. Nur wenn eine der beiden Listen leer ist, kann die jeweils andere Liste befüllt werden, sodass eine andere Tiergruppe essen kann. Dies wird über die folgenden beiden If-Abfragen gelöst:

```python
if (animal.name.startswith("Dog") or animal.name.startswith("Mouse")) and len(present_cats) == 0 and animal.name not in present_dogs_mouse:

```
```python
if animal.name.startswith("Cat") and len(present_dogs_mouse) == 0 and animal.name not in present_cats:
```

Innerhalb der For-Schleife wird dann überprüft, welche Liste aktuell einen Inhalt hat, und diese Gruppe wird dann zum Essen geschickt.

Sobald ein Tier den Status `SATISFIED` erreicht hat, wird es aus der Liste entfernt und solange ignoriert, bis das Tier wieder den Status `STARVING` hat.

```python
if a.name in present_dogs_mouse and a.get_status() == State.SATISFIED:
    present_dogs_mouse.remove(a.name)
```
```python
if a.name in present_cats and a.get_status() == State.SATISFIED:
    present_cats.remove(a.name)
```
