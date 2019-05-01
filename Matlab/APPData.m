clc; clear;close all;

a = textread('app.txt');
a = a(180:length(a)-1);


plot(-a)
