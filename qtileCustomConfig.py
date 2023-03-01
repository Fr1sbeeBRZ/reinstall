import os
import re
import socket
import subprocess
from typing import List  # noqa: F401
from libqtile import layout, bar, widget, hook
from libqtile.config import Click, Drag, Group, Key, Match, Screen, Rule
from libqtile.command import lazy
from libqtile.widget import Spacer

mod = "mod4"
mod1 = "alt"
mod2 = "control"
home = os.path.expanduser('~')


#! Swich between desktops

@lazy.function
def window_to_prev_group(qtile):
    if qtile.currentWindow is not None:
        i = qtile.groups.index(qtile.currentGroup)
        qtile.currentWindow.togroup(qtile.groups[i - 1].name)

@lazy.function
def window_to_next_group(qtile):
    if qtile.currentWindow is not None:
        i = qtile.groups.index(qtile.currentGroup)
        qtile.currentWindow.togroup(qtile.groups[i + 1].name)


#! keybindigs 

keys = [

    #? -------- Frecuent used apps --------
    Key([mod] , "n" , lazy.spawn("vivaldi-stable")),
    Key([mod] , "m" , lazy.spawn("rofi -show run")),
    Key([mod] , "t" , lazy.spawn("alacritty")),
    key([mod] , "s" , lazy.spawn("flameshot gui"))

    #? -------- IDEs keybinds --------
    Key(["control" , "shift"] , "v" , lazy.spawn("code")),
    Key(["control" , "shift"] , "j" , lazy.spawn("idea-ce")),
    Key(["control" , "shift"] , "p" , lazy.spawn("pycharm-ce")),
    Key(["control" , "shift"] , "a" , lazy.spawn("android-studio")),

    #? -------- Volumen keybinds --------
    Key(["mod" , "v"] , "+" , lazy.spawn("pamixer --increase 5")),
    Key(["mod" , "v"] , "-" , lazy.spawn("pamixer --decrease 5")),
    Key(["mod" , "v"] , "-" , lazy.spawn("pamixer --toggle-mute")),

    #? -------- Brightness keybinds --------
    Key(["mod" , "b"] , "+" , lazy.spawn("brightnessctl set +10%")),
    Key(["mod" , "b"] , "-" , lazy.spawn("brightnessctl set -10%")),

    #? -------- Window Options --------
    Key([mod], "f", lazy.window.toggle_fullscreen()),
    Key([mod], "q", lazy.window.kill()),
    Key([mod], "space", lazy.window.toggle_floating()),


    #? -------- Window Change Position --------
    Key([mod], "Up", lazy.layout.up()),
    Key([mod], "Down", lazy.layout.down()),
    Key([mod], "Left", lazy.layout.left()),
    Key([mod], "Right", lazy.layout.right()),

    #? -------- Reside Window --------
    Key([mod, "control"], "Right",
        lazy.layout.grow_right(),
        lazy.layout.grow(),
        lazy.layout.increase_ratio(),
        lazy.layout.delete(),
        ),
    Key([mod, "control"], "Left",
        lazy.layout.grow_left(),
        lazy.layout.shrink(),
        lazy.layout.decrease_ratio(),
        lazy.layout.add(),
        ),
    Key([mod, "control"], "Up",
        lazy.layout.grow_up(),
        lazy.layout.grow(),
        lazy.layout.decrease_nmaster(),
        ),
    Key([mod, "control"], "Down",
        lazy.layout.grow_down(),
        lazy.layout.shrink(),
        lazy.layout.increase_nmaster(),
        ),
]

def window_to_previous_screen(qtile, switch_group=False, switch_screen=False):
    i = qtile.screens.index(qtile.current_screen)
    if i != 0:
        group = qtile.screens[i - 1].group.name
        qtile.current_window.togroup(group, switch_group=switch_group)
        if switch_screen == True:
            qtile.cmd_to_screen(i - 1)

def window_to_next_screen(qtile, switch_group=False, switch_screen=False):
    i = qtile.screens.index(qtile.current_screen)
    if i + 1 != len(qtile.screens):
        group = qtile.screens[i + 1].group.name
        qtile.current_window.togroup(group, switch_group=switch_group)
        if switch_screen == True:
            qtile.cmd_to_screen(i + 1)

keys.extend([
    # MOVE WINDOW TO NEXT SCREEN
    Key([mod,"shift"], "Right", lazy.function(window_to_next_screen, switch_screen=True)),
    Key([mod,"shift"], "Left", lazy.function(window_to_previous_screen, switch_screen=True)),
])

#! Groups options
groups = []
group_names = ["1", "2", "3", "4", "5", "6", "7",]
group_labels = ["", "", "", "", "", "", "ﳠ",]
group_layouts = ["monadtall", "monadtall", "monadtall", "monadtall", "monadtall", "monadtall", "monadtall",]

for i in range(len(group_names)):
    groups.append(
        Group(
            name=group_names[i],
            layout=group_layouts[i].lower(),
            label=group_labels[i],
        ))

#! Change workspace
for i in groups:
    keys.extend([

        Key([mod], i.name, lazy.group[i.name].toscreen()),
        Key([mod], "Tab", lazy.screen.next_group()),
        Key([mod, "shift" ], "Tab", lazy.screen.prev_group()),
        Key(["mod1"], "Tab", lazy.screen.next_group()),
        Key(["mod1", "shift"], "Tab", lazy.screen.prev_group()),

        Key([mod, "shift"], i.name, lazy.window.togroup(i.name) , lazy.group[i.name].toscreen()),
    ])

def init_layout_theme():
    return {"margin":5,
            "border_width":2,
            "border_focus": "#5e81ac",
            "border_normal": "#4c566a"
            }

layout_theme = init_layout_theme()

layouts = [
    layout.MonadTall(**layout_theme),
    layout.MonadWide(**layout_theme),
    layout.Matrix(**layout_theme),
    layout.Bsp(**layout_theme),
    layout.Floating(**layout_theme),
    layout.RatioTile(**layout_theme),
    layout.Max(**layout_theme)
]

#! Bar palette