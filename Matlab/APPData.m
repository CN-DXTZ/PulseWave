clc; clear;close all;

a = textread('app2.txt');
a = a(10:end);

plot(-a)
