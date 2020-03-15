"""Hogwarts."""
from abc import abstractmethod
import random
from functools import reduce


class WizardrySchool:
    """Wizardry School."""

    def __init__(self, name: str, wizard_count: int):
        """Init."""
        self.name = name
        self.wizard_count = wizard_count
        self.wizard_list = []
        self.houses = {}

    @property
    def all_wizards(self):
        """Return a list of all of the wizards in the school."""
        return self.wizard_list + reduce(lambda total, house: total + house.wizard_list, self.houses)

    def check_wizard(self, wizard):
        """Check if wizard is suitable."""
        if not isinstance(wizard, Wizard):
            return 'This is not a wizard! It\'s muggle!'
        if not wizard.wand:
            return 'This wizard does not have a wand!'
        if not Wand.correct_wand(wizard.wand):
            return 'The wizard\'s wand is incorrect!'
        if wizard in self.wizard_list:
            return 'This wizard is already a student of this Wizardry School.'
        if len(self.wizard_list) >= self.wizard_count:
            return 'There is too much wizards in the school.'
        else:
            return False

    @abstractmethod
    def enrol_wizard(self, wizard):
        """Enroll wizard."""
        if not self.check_wizard(wizard):
            self.wizard_list.append(wizard)
            wizard.school = self
        else:
            return self.check_wizard(wizard)

    def unenrol_wizard(self, wizard):
        """Unenrol wizard."""
        if wizard in self.wizard_list:
            self.wizard_list.remove(wizard)
            wizard.school = None

    def add_house(self, house):
        """Add a new house to school."""
        free = self.wizard_count - sum(map(lambda house: house.wizard_count, self.houses)) - len(self.wizard_list)
        if isinstance(house, (Griffindor, Slitherin)):
            if house not in self.houses:
                if house.wizard_count <= free:
                    self.houses[house] = len(self.wizard_list)

    def remove_house(self, house):
        """Remove a house from school."""
        if house in self.houses:
            del self.houses[house]

    def get_wizard_list(self):
        """Get wizard list."""
        return self.all_wizards

    def get_wizards_of_house(self, name_of_house: str):
        """Get wizards of house."""
        list_of_name_and_object = list(map(lambda house: (house.name, house), self.houses))
        for house_tuple in list_of_name_and_object:
            if name_of_house == house_tuple[0]:
                house = house_tuple[1]
                return house.wizard_list

    def get_stat(self):
        """Get stat of amount of students in houses."""
        return self.houses

    def get_sorted_by_power(self):
        """Sort bThis is muggle school!y power."""
        return sorted(self.all_wizards, key=lambda wiz: wiz.power, reverse=True)

    def get_sorted_by_wand_score(self):
        """Sort by wand score."""
        return sorted(self.all_wizards, key=lambda wiz: wiz.wand.score, reverse=True)

    def get_sorted_by_fights(self):
        """Sort by fights."""
        return sorted(self.all_wizards, key=lambda wiz: wiz.fights, reverse=True)

    def punish_wizard(self, wizard):
        """Punish a wizard by sending him/her to the worst house."""
        the_worst = min(self.houses, key=lambda h: sum(map(lambda wiz: wiz.power, h.wizard_list)) // len(h.wizard_list))
        if wizard.school == the_worst:
            wizard.school.unenrol_wizard(wizard)
        else:
            wizard.school.unenrol_wizard(wizard)
            the_worst.enrol_wizard(wizard)

    def compliment_wizard_with_power(self, wizard):
        """Compliment wizard by adding power."""
        wizard.raw_power += max(round(5 * (70 - wizard.power) / 70), 0)

    def compliment_wizard_with_wand(self, wizard):
        """Compliment wizard with wand."""
        wand = wizard.wand
        wizard.update_wand(Wand(wand.wood_type, wand.core, max(wizard.power // 8, wand.score + 1), wand.owner))

    def fight(self, wizard1, wizard2):
        """Fight between 2 wizards."""
        decide_dict = {wizard1: 0, wizard2: 0}
        for wiz in wizard1, wizard2:
            if isinstance(wiz.school, Griffindor):
                decide_dict[wiz] = wiz.power * 13 + wiz.wand.score * 10
            elif isinstance(wiz.school, Slitherin):
                decide_dict[wiz] = wiz.power + wiz.wand.score * 100
            else:
                decide_dict[wiz] = wiz.power * 10 + wiz.wand.score * 10
            wiz.fights += 1
        self.pay_respect(self.get_winner(decide_dict))
        return f'{self.get_winner(decide_dict)} won!'

    def get_winner(self, decide_dict):
        """Get winner."""
        return max(decide_dict, key=lambda wizard: decide_dict[wizard])

    def pay_respect(self, winner):
        """Press "F" to."""
        if isinstance(winner.school, Slitherin):
            self.compliment_wizard_with_wand(winner)
        else:
            self.compliment_wizard_with_power(winner)

    def __str__(self):
        """Str."""
        return f'{self.name} is a Wizardry School with {self.wizard_count} students.'

    def __repr__(self):
        """Repr."""
        return f'{self.name}'


class Griffindor(WizardrySchool):
    """House1."""

    def enrol_wizard(self, wizard):
        """Enrol wizard."""
        if not self.check_wizard(wizard) and not self.check_griffindor(wizard):
            self.wizard_list.append(wizard)
        else:
            return self.check_wizard(wizard)

    def check_griffindor(self, wizard):
        """Check wizard."""
        if wizard.power < 25:
            return f'The wizard power is too low for {self.name}!'
        if wizard.name.startswith('S') or 'Malfoy' in wizard.name:
            return f'It is the enemy of {self.name}!!!'
        else:
            return False

    def power(self, wizard):
        """Power grows."""
        power_average_in_house = sum(map(lambda wiz: wiz.power, self.wizard_list)) // len(self.wizard_list)
        if wizard.power <= power_average_in_house:
            wizard.raw_power += power_average_in_house // 2


class Slitherin(WizardrySchool):
    """House2."""

    def enrol_wizard(self, wizard):
        """Enrol wizard to Slitherin House."""
        if not self.check_wizard(wizard) and wizard.blood_pureness >= 60:
            self.wizard_list.append(wizard)
        else:
            return self.check_wizard(wizard)

    def check_slitherin(self, wizard):
        """Check wizard."""
        if wizard.blood_pureness < 60:
            return f'Die, you child of muggles!'
        if 's' not in wizard.name.lower():
            return f'It is the enemy of {self.name}!!!'
        else:
            return False

    def slitherin_punishment(self):
        """Punish some slitherenians."""
        the_weakest = min(filter(lambda wiz: 'Malfoy' not in wiz.name, self.wizard_list), key=lambda wiz: wiz.power)
        wand = the_weakest.wand
        the_weakest.update_wand(Wand(wand.wood_type, wand.core, max(wand.score - 2, 1), wand.owner))


class Wizard:
    """Wizard."""

    def __init__(self, name: str, power: int, fights: int, school: WizardrySchool = None, wand=None):
        """Init."""
        self.name = name
        self.raw_power = power
        self.fights = fights
        self.wand = wand
        self.blood_pureness = random.randint(25, 100)

        if wand:
            Wand.correct_wand(wand)

        if isinstance(school, WizardrySchool) and not isinstance(school.enrol_wizard(self), str):
            self.school = school
        else:
            raise MismatchError('This is muggle school!')

    @property
    def power(self):
        """Power in range(101)."""
        if self.raw_power < 0:
            power = 1
        elif self.raw_power > 100:
            power = 100
        else:
            power = self.raw_power
        return power

    def update_wand(self, wand):
        """Change wizard's wand."""
        if wand:
            Wand.correct_wand(wand)
        self.wand = wand

    def __str__(self):
        """Str."""
        return f'Wizard {self.name} with wand of type {self.wand.wood_type} and power {self.power}'

    def __repr__(self):
        """Repr."""
        return f'{self.name}'


class Wand:
    """Wand."""

    def __init__(self, wood_type: str, core: str, score: int, owner: Wizard = None):
        """Init."""
        self.wood_type = wood_type
        self.core = core
        self.rawscore = score
        self.owner = owner

    @property
    def score(self):
        """Score in range(14)."""
        return self.rawscore if self.rawscore in range(14) else round(12 ** (self.rawscore / abs(self.rawscore))) + 1

    def change_wood_type(self, wood_type: str):
        """Change wood type."""
        self.wood_type = wood_type

    def change_core(self, core: str):
        """Change core."""
        self.core = core

    @staticmethod
    def correct_wand(wand):
        """Check if the wand is correct."""
        if not isinstance(wand, Wand) or not wand.wood_type or not wand.core:
            raise MismatchError('The wand like that does not exist!')
        return True

    def __str__(self):
        """Str."""
        return f'Wand type: {self.wood_type}, core: {self.core}, score: {self.score}, owner: {self.owner}.'

    def __repr__(self):
        """Repr."""
        return f'{self.wood_type}, {self.core}, {self.score}, {self.owner}.'


class MismatchError(Exception):
    """Exception."""

    pass


if __name__ == '__main__':
    school = WizardrySchool('Hogwarts', 100)
    house1 = Griffindor('Griffindor', 20)
    house2 = Slitherin('Slitherin', 20)
    school.add_house(house1)
    school.add_house(house2)
    wand1 = Wand('wood', 'core', 7)
    wand2 = Wand('wood', 'core', 7)
    harry = Wizard('Harry', 50, 0, house1, wand1)
    malfoy = Wizard('Draco', 50, 0, house2, wand2)

    print(school.fight(harry, malfoy))
    print(harry)
    print(wand1)
